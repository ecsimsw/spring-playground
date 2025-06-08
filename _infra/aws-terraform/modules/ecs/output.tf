output "ecs_task_execution_role" {
  value = aws_iam_role.ecs_task_execution_role.arn
}

output "cluster_id" {
  value = aws_ecs_cluster.ecs_cluster.id
}

output "ecs_security_group_id" {
  value = aws_security_group.ecs_sg.id
}