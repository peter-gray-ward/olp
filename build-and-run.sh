#!/bin/bash

echo "🚀 Building all microservices..."

# Stop script if any command fails
set -e

# Define microservices
MICROSERVICES=("olp-config" "olp-authentication" "olp-courses" "olp-gateway" "olp-notifications")

# Loop through each microservice and build it
for SERVICE in "${MICROSERVICES[@]}"; do
  echo "📦 Packaging $SERVICE..."
  cd $SERVICE
  mvn clean package -DskipTests
  cd ..
done

echo "✅ All microservices packaged successfully!"

echo "🐳 Building and starting Docker containers..."

# Build and run Docker Compose
docker image prune -a -f
docker-compose down --rmi all --volumes --remove-orphans
docker-compose build --no-cache
docker-compose up
