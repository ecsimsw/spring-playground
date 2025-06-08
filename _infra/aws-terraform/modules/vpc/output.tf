output "vpc_id" {
  value = aws_vpc.vpc_main.id
}

output "public_subnet_ids" {
  value = [
    aws_subnet.subnet_public_2a.id,
    aws_subnet.subnet_public_2c.id,
  ]
}

output "private_subnet_ids" {
  value = [
    aws_subnet.subnet_private_2a.id,
    aws_subnet.subnet_private_2c.id
  ]
}