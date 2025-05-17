resource "aws_lb" "external_nlb" {
  name               = "spring-playground-external-nlb"
  internal           = false
  load_balancer_type = "network"
  subnets            = var.public_subnet_ids
}

resource "aws_lb_listener" "external_nlb_listener" {
  load_balancer_arn = aws_lb.external_nlb.arn
  port              = 8080
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.external_nlb_tg.arn
  }
}

resource "aws_lb_target_group" "external_nlb_tg" {
  name        = "spring-playground-nlb-to-alb-tg"
  target_type = "alb"
  port        = 8080
  protocol    = "TCP"
  vpc_id      = var.vpc_id

  health_check {
    port                = "8080"
    protocol            = "HTTP"
    path                = "/api/auth/up"
    matcher             = "200-399"
    interval            = 30
    unhealthy_threshold = 3
    healthy_threshold   = 3
  }
}

resource "aws_lb_target_group_attachment" "attach_alb" {
  target_group_arn = aws_lb_target_group.external_nlb_tg.arn
  target_id        = aws_lb.internal_alb.arn
  port             = 8080
}
