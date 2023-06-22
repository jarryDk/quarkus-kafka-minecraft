#!/bin/bash

source ./config.conf

podman run -d \
    --name=$KAFKA_SERVER_HOST \
    --network=$PODMAN_NETWORK \
    -p 9092:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=$KAFKA_ZOOKEEPER_HOST:2181 \
    docker.io/jarrydk/fedora-minimal-kafka:3.5.0
