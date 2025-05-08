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
  source    = "./modules/ecs"
  alb_sg_id = module.lb.alb_sg_id
  vpc_id    = module.vpc.vpc_id
}

module "lb" {
  source            = "./modules/lb"
  vpc_id            = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
}

module "notification" {
  source                  = "./modules/service/notification"
  vpc_id                  = module.vpc.vpc_id
  alb_arn                 = module.lb.alb_arn
  alb_listener_arn        = module.lb.alb_listener_arn
  private_subnet_ids      = module.vpc.private_subnet_ids
  cluster_id              = module.ecs.cluster_id
  ecr_url                 = module.ecr.ecr_url
  ecs_security_group_id   = module.ecs.ecs_security_group_id
  ecs_task_execution_role = module.ecs.ecs_task_execution_role
}