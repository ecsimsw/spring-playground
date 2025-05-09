#!/bin/bash

# ./build-and-deploy.sh api-config 1.0.0

if [ $# -ne 2 ]; then
  echo "use : $0 <MODULE_NAME> <VERSION>"
  echo "ex, $0 api-config 1.0.0"
  exit 1
fi

REPO_NAME="spring-playground"
MODULE_NAME="$1"
VERSION="$2"
ROOT_DIR="../"

(cd "$ROOT_DIR" && ./gradlew ":_$MODULE_NAME:build")

JAR_PATH="../_$MODULE_NAME/build/libs/_$MODULE_NAME-$VERSION.jar"
IMAGE_TAG="${MODULE_NAME}-${VERSION}"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
AWS_REGION=$(aws configure get region)
ECR_IMAGE="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}"

if [ ! -f "$JAR_PATH" ]; then
  echo "Not exists file : $JAR_PATH"
  exit 1
fi

cp "$JAR_PATH" ./app/app.jar

docker buildx build \
 --platform linux/amd64 \
 -t "${REPO_NAME}:${IMAGE_TAG}" --load \
 -f ./app/Dockerfile ./app

BUILD_RESULT=$?
if [ $BUILD_RESULT -ne 0 ]; then
  echo "Docker build failed!"
  exit 2
fi

rm ./app/app.jar
aws ecr get-login-password | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker tag "${REPO_NAME}:${IMAGE_TAG}" "${ECR_IMAGE}"
docker push "${ECR_IMAGE}"

docker rmi "${REPO_NAME}:${IMAGE_TAG}" 2>/dev/null || true
docker rmi "${ECR_IMAGE}" 2>/dev/null || true