# TARGET_SERVICE

resource "aws_lb_target_group" "aws_lb_tg_notification" {
  name        = "spground-notification-${substr(uuid(), 0, 3)}"
  port        = var.notification_port

  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    path                = "/api/notification/up"
    interval            = 30
    timeout             = 10
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

# LOG_GROUP

resource "aws_cloudwatch_log_group" "notification" {
  name              = "/spring-playground/notification-svc"
  retention_in_days = 1
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
      name   = "spring-notification-svc"
      image  = "${var.ecr_url}:api-notification-${var.notification_version}"
      cpu    = 256
      memory = 512
      essential = true # If the essential parameter of a container is marked as true, and that container fails or stops for any reason, all other containers that are part of the task are stopped
      portMappings = [
        {
          containerPort = var.notification_port
          hostPort      = var.notification_port
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = aws_cloudwatch_log_group.notification.name
          awslogs-region        = "ap-northeast-2"
          awslogs-stream-prefix = "notification"
        }
      }

      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name  = "VERSION"
          value = var.notification_version
        }
      ]
    }
  ])

  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }
}

# ECS_SG

resource "aws_security_group" "ecs_notification_sg" {
  name   = "spring-playground-ecs-notification-sg"
  vpc_id = var.vpc_id

  ingress {
    from_port       = var.notification_port
    to_port         = var.notification_port
    protocol        = "tcp"
    security_groups = [var.internal_alb_sg_id] # Allow only for alb
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ECS_SERVICE

resource "aws_ecs_service" "ecs_service_notification" {
  name            = "spring-playground-notification"
  cluster         = var.cluster_id
  task_definition = aws_ecs_task_definition.ecs_task_notification.arn
  desired_count   = 1
  launch_type     = null
  health_check_grace_period_seconds = 120
  force_new_deployment = true

  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups = [
      var.ecs_security_group_id,
      aws_security_group.ecs_notification_sg.id
    ]
    assign_public_ip = false
  }

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 1
    base              = 0
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.aws_lb_tg_notification.arn
    container_name   = "spring-notification-svc"  # make sure that set same as container name
    container_port   = var.notification_port
  }

  depends_on = [
    var.alb_listener_arn
  ]
}
