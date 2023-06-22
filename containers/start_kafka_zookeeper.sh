#!/bin/bash

source ./config.conf

podman run -d \
    --name=$KAFKA_ZOOKEEPER_HOST \
    --network=$PODMAN_NETWORK \
    -p 2181:2181 \
    docker.io/jarrydk/fedora-minimal-kafka-zookeeper:3.5.0
