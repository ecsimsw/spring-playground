# TARGET_SERVICE

resource "aws_lb_target_group" "aws_lb_tg_notification" {
  name        = "playground-notification-${substr(uuid(), 0, 5)}"
  port        = 8083
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    path                = "/up"
    interval            = 5
    timeout             = 2
    healthy_threshold   = 2
    unhealthy_threshold = 2
    matcher             = "200"
  }

  lifecycle {
    ignore_changes = [name]
  }
}

resource "aws_lb_listener_rule" "alb_listener_rule_notification" {
  listener_arn = var.alb_listener_arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.aws_lb_tg_notification.arn
  }

  condition {
    path_pattern {
      values = ["/api/notification/*"]
    }
  }
}

# ECS_TASK

resource "aws_ecs_task_definition" "ecs_task_notification" {
  family             = "task-notification"
  execution_role_arn = var.ecs_task_execution_role
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                = 256
  memory             = 512

  container_definitions = jsonencode([
    {
      name   = "notification-svc"
      image  = "${var.ecr_url}:notification-0.0.1"
      cpu    = 256
      memory = 512
      essential = true # If the essential parameter of a container is marked as true, and that container fails or stops for any reason, all other containers that are part of the task are stopped
      portMappings = [
        {
          containerPort = 8083
          hostPort      = 8083
          protocol      = "tcp"
        }
      ]
    }
  ])

  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }
}

# ECS_SERVICE

resource "aws_ecs_service" "ecs_service_notification" {
  name            = "sp-notification"
  cluster         = var.cluster_id
  task_definition = aws_ecs_task_definition.ecs_task_notification.arn
  desired_count   = 1
  launch_type     = null
  health_check_grace_period_seconds = 120
  force_new_deployment = true

  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups = [var.ecs_security_group_id]
    assign_public_ip = false
  }

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 1
    base              = 0
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.aws_lb_tg_notification.arn
    container_name   = "notification-svc"  # make sure that set same as container name
    container_port   = 8083
  }

  depends_on = [
    var.alb_listener_arn
  ]
}
