variable "vpc_id" {
  type = string
}

variable "internal_alb_sg_id" {
  type = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "ecr_url" {
  type = string
}

variable "ecs_task_execution_role" {
  type = string
}

variable "cluster_id" {
  type = string
}

variable "ecs_security_group_id" {
  type = string
}

variable "alb_listener_arn" {
  type = string
}

variable "account_port" {
  type = number
}

variable "account_version" {
  type = string
}

variable "device_port" {
  type = number
}

variable "device_version" {
  type = string
}

variable "notification_port" {
  type = number
}

variable "notification_version" {
  type = string
}

variable "event_port" {
  type = number
}

variable "event_version" {
  type = string
}