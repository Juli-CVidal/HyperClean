#!/bin/bash

echo "Starting docker compose"
docker compose up -d
while ! docker-compose ps | grep -q "hyperclean.*Up"; do
  echo "Waiting for the container to be ready..."
  sleep 2
done

echo "Starting backend..."
cd ./hyperclean && ./mvnw spring-boot:run