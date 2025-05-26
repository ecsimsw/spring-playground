## Terraform

### Cheat sheet

#### Force refresh
```
terraform taint module.ecs.aws_ecs_service.ecs_service_account
terraform taint module.ecs.aws_ecs_service.ecs_service_device
terraform taint module.ecs.aws_ecs_service.ecs_service_notification

terraform apply
```

### Error, ecs-task-execution-role already exists.
```
terraform import module.ecs.aws_iam_role.ecs_task_execution_role ecs-task-execution-role
```
