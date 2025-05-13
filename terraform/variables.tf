variable "aws_region" {
  type        = string
  default     = "us-east-1"
  description = "AWS Account region"
}

variable "eks_cluster_name" {
  type        = string
  default     = "fiap-tech-challenge-eks-cluster"
  description = "EKS Cluster name"
}

variable "ecr_repository_name" {
  type        = string
  default     = "payment-api"
  description = "AWS ECR repository name"
}

variable "server_port" {
  type        = number
  default     = 8091
  description = "Payment App server port"
}

variable "dynamo_db_name" {
  type        = string
  default     = "order_payment_entity"
  description = "Dynamo DB Table name"
}

variable "AWS_ACCESS_KEY_ID" {
  type        = string
  description = "aws_access_key_id"
}

variable "AWS_SECRET_ACCESS_KEY" {
  type        = string
  description = "aws_secret_access_key"
}

variable "AWS_SESSION_TOKEN" {
  type        = string
  description = "aws_session_token"
}