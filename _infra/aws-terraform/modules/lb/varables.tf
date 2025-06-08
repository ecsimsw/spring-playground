variable "vpc_id" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "internal_lb_cidr_block" {
  type = list(string)
}

variable "private_subnet_ids" {
  type = list(string)
}
