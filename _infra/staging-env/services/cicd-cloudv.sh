#!/bin/bash

if [ $# -ne 1 ]; then
  echo "use : $0 <MODULE_NAME>"
  echo "ex, $0 api-config"
  exit 1
fi

REPO_NAME="hejhome-pro"
MODULE_NAME="$1"
ROOT_DIR="../../.."
MODULE_GRADLE_FILE="${ROOT_DIR}/_api/$MODULE_NAME/build.gradle"

if [ ! -f "$MODULE_GRADLE_FILE" ]; then
  echo "Not exists build.gradle : $MODULE_GRADLE_FILE"
  exit 1
fi

VERSION=$(grep "^version" "$MODULE_GRADLE_FILE" | head -n1 | sed "s/version *= *['\"]\([^'\"]*\)['\"]/\1/")

if [ -z "$VERSION" ]; then
  echo "Failed to extract version from $MODULE_GRADLE_FILE"
  exit 1
fi

echo "Version found: $VERSION"

JAR_PATH="${ROOT_DIR}/_api/$MODULE_NAME/build/libs/$MODULE_NAME-$VERSION.jar"
IMAGE_TAG="${MODULE_NAME}-latest"
rm -rf "${ROOT_DIR}/_api/$MODULE_NAME/build/libs/"

(cd "$ROOT_DIR" && ./gradlew ":_api:$MODULE_NAME:build")
BUILD_RESULT=$?
if [ $BUILD_RESULT -ne 0 ]; then
  echo "Gradle build failed!"
  exit 2
fi

if [ ! -f "$JAR_PATH" ]; then
  echo "Not exists file : $JAR_PATH"
  exit 1
fi

cp "$JAR_PATH" ./app.jar

docker buildx build \
 --platform linux/amd64 \
 -t "${REPO_NAME}:${IMAGE_TAG}" --load \
 -f ./Dockerfile .

BUILD_RESULT=$?
if [ $BUILD_RESULT -ne 0 ]; then
  echo "Docker build failed!"
  rm -f ./app.jar
  exit 2
fi

rm -f ./app.jar

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
AWS_REGION=$(aws configure get region)
ECR_IMAGE="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}"

aws ecr get-login-password | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker tag "${REPO_NAME}:${IMAGE_TAG}" "${ECR_IMAGE}"
docker push "${ECR_IMAGE}"

docker rmi "${REPO_NAME}:${IMAGE_TAG}" 2>/dev/null || true
docker rmi "${ECR_IMAGE}" 2>/dev/null || true

SERVER_USER=
SERVER_IP=
SERVER_DIR=

ssh ${SERVER_USER}@${SERVER_IP} bash -c "'
  set -e
  cd ${SERVER_DIR}
  export AWS_ACCOUNT_ID=\$(aws sts get-caller-identity --query \"Account\" --output text)
  export AWS_REGION=\$(aws configure get region)
  aws ecr get-login-password --region \${AWS_REGION} | docker login --username AWS --password-stdin \${AWS_ACCOUNT_ID}.dkr.ecr.\${AWS_REGION}.amazonaws.com

  echo \"Redeploy: $MODULE_NAME\"
  docker compose pull $MODULE_NAME
  docker compose stop $MODULE_NAME
  docker compose rm -f $MODULE_NAME
  docker compose up -d $MODULE_NAME

  echo \"Waiting 20 seconds for containers to become ready...\"
  sleep 20

  if [[ \"$MODULE_NAME\" == \"account\" || \"$MODULE_NAME\" == \"device\" || \"$MODULE_NAME\" == \"event\" ]]; then
    cd ./apidocs

    rm -rf files/
    mkdir files

    wget http://$SERVER_IP:8080/api/account/api-docs -O ./files/account.json || true
    wget http://$SERVER_IP:8080/api/device/api-docs -O ./files/device.json || true
    wget http://$SERVER_IP:8080/api/event/api-docs -O ./files/event.json || true

    openapi-merge-cli --config ./merge-config.json
    cd -

    docker compose stop swagger
    docker compose rm -f swagger
    docker compose up -d swagger
    echo \"Swagger UI refreshed.\"
  fi
'"

echo "=== ✅ [$MODULE_NAME:$VERSION] 배포 완료! ==="