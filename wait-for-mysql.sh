#!/bin/sh
set -e

echo "Waiting for MySQL to be ready..."
until mysqladmin ping -h mysql-container -u"$SPRING_DATASOURCE_USERNAME" -p"$SPRING_DATASOURCE_PASSWORD" --silent; do
  sleep 3
done

echo "MySQL is up - starting application"
