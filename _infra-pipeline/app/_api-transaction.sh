## ./_api-transaction 1.0.0

if [ -z "$1" ]; then
  exit 1
fi

VERSION="$1"
MODULE_NAME="_api-transaction"
JAR_PATH="../../$MODULE_NAME/build/libs/$MODULE_NAME-$VERSION.jar"

if [ ! -f "$JAR_PATH" ]; then
  echo "Not exists file : $JAR_PATH"
  exit 1
fi

cp "$JAR_PATH" app.jar
docker build -t "spring-playground$MODULE_NAME:$VERSION" .
rm app.jar