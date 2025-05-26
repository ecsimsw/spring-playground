terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0.0"
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}

module "vpc" {
  source = "./modules/vpc"
}

module "ecr" {
  source = "./modules/ecr"
}

module "ecs" {
  source                  = "./modules/ecs"
  internal_alb_sg_id      = module.lb.internal_alb_sg_id
  vpc_id                  = module.vpc.vpc_id
  alb_listener_arn        = module.lb.internal_alb_listener_arn
  private_subnet_ids      = module.vpc.private_subnet_ids
  cluster_id              = module.ecs.cluster_id
  ecr_url                 = module.ecr.ecr_url
  ecs_security_group_id   = module.ecs.ecs_security_group_id
  ecs_task_execution_role = module.ecs.ecs_task_execution_role
  account_port            = 8851
  account_version         = "1.0.13"
  device_port             = 8852
  device_version          = "1.0.3"
  notification_port       = 8854
  notification_version    = "1.0.7"
}

module "lb" {
  source             = "./modules/lb"
  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids
  internal_lb_cidr_block = ["0.0.0.0/0"]
}
