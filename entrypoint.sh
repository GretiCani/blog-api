#!/bin/bash
set -a
source /app/.env
set +a
exec java -jar /app/blog-api.jar
