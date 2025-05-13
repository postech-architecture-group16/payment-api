resource "kubernetes_namespace" "payment_namespace" {
  metadata {
    name = "payment"
  }
}

resource "kubernetes_secret" "payment_secret" {
  metadata {
    name      = "tech-challenge-payment-secret"
    namespace = kubernetes_namespace.payment_namespace.metadata[0].name
  }

  data = {
    db-table              = var.dynamo_db_name
    aws_access_key_id     = var.AWS_ACCESS_KEY_ID
    aws_secret_access_key = var.AWS_SECRET_ACCESS_KEY
    aws_session_token     = var.AWS_SESSION_TOKEN
  }

  type = "Opaque"

  depends_on = [kubernetes_namespace.payment_namespace]
}

resource "kubernetes_deployment" "payment_deployment" {
  metadata {
    name      = "payment-api"
    namespace = kubernetes_namespace.payment_namespace.metadata[0].name
    labels = {
      app = "payment-api"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "payment-api"
      }
    }

    template {
      metadata {
        labels = {
          app = "payment-api"
        }
      }

      spec {
        container {
          image             = data.aws_ecr_image.latest_image.image_uri
          name              = "payment-api"
          image_pull_policy = "Always"

          resources {
            limits = {
              cpu    = "500m"
              memory = "1Gi"
            }
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/payment-api/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 30
            timeout_seconds       = 5
            failure_threshold     = 3
          }

          readiness_probe {
            http_get {
              path = "/payment-api/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 10
            timeout_seconds       = 3
            failure_threshold     = 1
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "default"
          }


          env {
            name = "SPRING_DATASOURCE_URL"
            value_from {
              secret_key_ref {
                name = "tech-challenge-payment-secret"
                key  = "db-table"
              }
            }
          }

          env {
            name = "EXTERNAL_API_TOKEN"
            value_from {
              secret_key_ref {
                name = "tech-challenge-payment-secret"
                key  = "external-api-token"
              }
            }
          }

          env {
            name  = "QUEUE_NAME_LISTENER"
            value = "order-payment"
          }

          env {
            name  = "QUEUE_NAME_PRODUCER"
            value = data.aws_sqs_queue.payment-order-queue.url
          }

          env {
            name = "AWS_ACCESS_KEY_ID"
            value_from {
              secret_key_ref {
                name = "tech-challenge-payment-secret"
                key  = "aws_access_key_id"
              }
            }
          }

          env {
            name = "AWS_SECRET_ACCESS_KEY"
            value_from {
              secret_key_ref {
                name = "tech-challenge-payment-secret"
                key  = "aws_secret_access_key"
              }
            }
          }

          env {
            name = "AWS_SESSION_TOKEN"
            value_from {
              secret_key_ref {
                name = "tech-challenge-payment-secret"
                key  = "aws_session_token"
              }
            }
          }

          env {
            name  = "AWS_REGION"
            value = "us-east-1"
          }
        }
      }
    }
  }

  timeouts {
    create = "4m"
    update = "4m"
    delete = "4m"
  }

  depends_on = [kubernetes_secret.payment_secret]


}

resource "kubernetes_service" "payment_service" {
  metadata {
    name      = "payment-api-service"
    namespace = kubernetes_namespace.payment_namespace.metadata[0].name
  }

  spec {
    selector = {
      app = "payment-api"
    }

    port {
      port        = var.server_port
      target_port = var.server_port
    }

    cluster_ip = "None"
  }
}

resource "kubernetes_ingress_v1" "payment_ingress" {
  metadata {
    name      = "payment-api-ingress"
    namespace = kubernetes_namespace.payment_namespace.metadata[0].name

    annotations = {
      "nginx.ingress.kubernetes.io/x-forwarded-port" = "true"
      "nginx.ingress.kubernetes.io/x-forwarded-host" = "true"
    }
  }

  spec {
    ingress_class_name = "nginx"

    rule {
      http {
        path {
          path      = "/payment-api"
          path_type = "Prefix"

          backend {
            service {
              name = "payment-api-service"
              port {
                number = var.server_port
              }
            }
          }
        }
      }
    }
  }

  depends_on = [kubernetes_service.payment_service]

}

resource "kubernetes_horizontal_pod_autoscaler_v2" "payment_hpa" {
  metadata {
    name      = "payment-api-hpa"
    namespace = kubernetes_namespace.payment_namespace.metadata[0].name
  }

  spec {
    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = "payment-api"
    }

    min_replicas = 1
    max_replicas = 5

    metric {
      type = "Resource"

      resource {
        name = "cpu"
        target {
          type                = "Utilization"
          average_utilization = 75
        }
      }
    }
  }

  depends_on = [kubernetes_service.payment_service]

}