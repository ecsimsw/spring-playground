#!/bin/bash

ACCOUNT_VERSION="1.0.23"
DEVICE_VERSION="1.0.32"
NOTIFICATION_VERSION="1.0.17"
EVENT_VERSION="1.0.22"

cp .env .env.bak

sed -i "s/^ACCOUNT_VERSION=.*/ACCOUNT_VERSION=${ACCOUNT_VERSION}/" .env
sed -i "s/^DEVICE_VERSION=.*/DEVICE_VERSION=${DEVICE_VERSION}/" .env
sed -i "s/^NOTIFICATION_VERSION=.*/NOTIFICATION_VERSION=${NOTIFICATION_VERSION}/" .env
sed -i "s/^EVENT_VERSION=.*/EVENT_VERSION=${EVENT_VERSION}/" .env

echo "Pull service container images"

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
AWS_REGION=$(aws configure get region)
aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

echo "Restart containers"

docker compose down
docker compose pull
docker compose up -d

echo "Waiting 30 seconds for containers to become ready..."
sleep 30

wget http://hejdev1.goqual.com:8080/api/account/api-docs -O ./apidocs/files/account.json
wget http://hejdev1.goqual.com:8080/api/device/api-docs -O ./apidocs/files/device.json
wget http://hejdev1.goqual.com:8080/api/notification/api-docs -O ./apidocs/files/notification.json
wget http://hejdev1.goqual.com:8080/api/event/api-docs -O ./apidocs/files/event.json

cd ./apidocs
openapi-merge-cli --config ./merge-config.json
cd -

docker rm -f swagger-ui || true
docker run -d --name swagger-ui -p 8874:8080 \
  -e SWAGGER_JSON=/files/merged.json \
  -v $PWD/apidocs/files:/files \
  swaggerapi/swagger-ui

echo "Finished CI/CD"
