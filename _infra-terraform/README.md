## Terraform

### Cheat sheet
```
terraform taint module.ecs.aws_ecs_service.ecs_service_account
terraform taint module.ecs.aws_ecs_service.ecs_service_auth
terraform taint module.ecs.aws_ecs_service.ecs_service_notification

terraform apply
```