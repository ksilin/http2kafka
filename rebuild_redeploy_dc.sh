#!/bin/sh

gradle build -x test --no-daemon
docker build -f src/main/docker/Dockerfile.jvm -t example/http2kafka .
docker compose up -d