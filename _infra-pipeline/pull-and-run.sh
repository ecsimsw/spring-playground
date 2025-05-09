#!/bin/bash

if [ $# -ne 3 ]; then
  echo "Usage: $0 <MODULE_NAME> <VERSION> <EXTERNAL_PORT>"
  echo "예시: $0 api-config 1.0.0 8850"
  exit 1
fi

REPO_NAME="spring-playground"
MODULE_NAME="$1"
VERSION="$2"
PORT="$3"
IMAGE_TAG="${MODULE_NAME}-${VERSION}"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
AWS_REGION=$(aws configure get region)
ECR_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}"

aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
docker pull "${ECR_URI}"
docker run -d --rm --name "${MODULE_NAME}-${VERSION}" -p "${PORT}:${PORT}" "${ECR_URI}"

