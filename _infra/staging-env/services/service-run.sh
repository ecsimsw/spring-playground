#!/bin/bash

ACCOUNT_VERSION="1.0.23"
DEVICE_VERSION="1.0.31"
NOTIFICATION_VERSION="1.0.15"
EVENT_VERSION="1.0.20"

cp .env .env.bak

sed -i "s/^ACCOUNT_VERSION=.*/ACCOUNT_VERSION=${ACCOUNT_VERSION}/" .env
sed -i "s/^DEVICE_VERSION=.*/DEVICE_VERSION=${DEVICE_VERSION}/" .env
sed -i "s/^NOTIFICATION_VERSION=.*/NOTIFICATION_VERSION=${NOTIFICATION_VERSION}/" .env
sed -i "s/^EVENT_VERSION=.*/EVENT_VERSION=${EVENT_VERSION}/" .env

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
AWS_REGION=$(aws configure get region)
aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker compose down
docker compose pull
docker compose up -d
