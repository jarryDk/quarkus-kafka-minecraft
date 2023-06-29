#!/bin/bash

source ./config.conf

podman run -d \
    --name=$KAFKA_SERVER_HOST \
    --network=$PODMAN_NETWORK \
    -p 9092:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=$KAFKA_ZOOKEEPER_HOST:2181 \
    -e KAFKA_LISTENERS=PLAINTEXT://:9092 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://broker1.jarry.dk:9092 \
    -e KAFKA_BROKER_ID=1 \
    docker.io/jarrydk/fedora-minimal-kafka:3.5.0