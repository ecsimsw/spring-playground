# VPC
resource "aws_vpc" "vpc_main" {
  cidr_block = "10.1.0.0/16"

  tags = {
    Name = "spring-playground-vpc"
  }
}

# IGW
resource "aws_internet_gateway" "vpc_igw_main" {
  vpc_id = aws_vpc.vpc_main.id
}

# NAT
resource "aws_eip" "nat_ip_main" {
  domain = "vpc"

  tags = {
    Name = "spring-playground-nat"
  }
}

resource "aws_nat_gateway" "nat_main" {
  allocation_id = aws_eip.nat_ip_main.id
  subnet_id     = aws_subnet.subnet_public_2a.id
}

# SUBNETS
resource "aws_subnet" "subnet_public_2a" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.1.1.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "subnet_public_2c" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.1.2.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "subnet_private_2a" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.1.3.0/24"
  availability_zone       = "ap-northeast-2a"
}

resource "aws_subnet" "subnet_private_2c" {
  vpc_id                  = aws_vpc.vpc_main.id
  cidr_block              = "10.1.4.0/24"
  availability_zone       = "ap-northeast-2c"
}

# ROUTE_TABLE
resource "aws_route_table" "route_table_public" {
  vpc_id = aws_vpc.vpc_main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.vpc_igw_main.id
  }
}

resource "aws_route_table" "route_table_private" {
  vpc_id = aws_vpc.vpc_main.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_main.id
  }
}

resource "aws_route_table_association" "rt_assoc_public_2a" {
  subnet_id      = aws_subnet.subnet_public_2a.id
  route_table_id = aws_route_table.route_table_public.id
}

resource "aws_route_table_association" "rt_assoc_public_2c" {
  subnet_id      = aws_subnet.subnet_public_2c.id
  route_table_id = aws_route_table.route_table_public.id
}

resource "aws_route_table_association" "rt_assoc_private_2a" {
  subnet_id      = aws_subnet.subnet_private_2a.id
  route_table_id = aws_route_table.route_table_private.id
}

resource "aws_route_table_association" "rt_assoc_private_2c" {
  subnet_id      = aws_subnet.subnet_private_2c.id
  route_table_id = aws_route_table.route_table_private.id
}