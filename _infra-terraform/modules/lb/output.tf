output "internal_alb_arn" {
  value = aws_lb.internal_alb.arn
}

output "internal_alb_sg_id" {
  value = aws_security_group.internal_alb_sg.id
}

output "internal_alb_listener_arn" {
  value = aws_lb_listener.internal_alb_listener.arn
}
