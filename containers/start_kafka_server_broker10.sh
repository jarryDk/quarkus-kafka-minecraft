#!/bin/bash

source ./config.conf

podman volume create \
    --ignore \
    --label kafka=minecraft \
    kafka-server-broker10-data

podman run -d \
    --name=kafka-server-broker10 \
    --network=$PODMAN_NETWORK \
    -p 127.0.2.30:9092:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=$KAFKA_ZOOKEEPER_HOST:2181 \
    -e KAFKA_LISTENERS="PLAINTEXT://:9092,INTERNAL_PLAINTEXT://:9094" \
    -e KAFKA_LISTENER_SECURITY_PROTOCAL_MAP="PLAINTEXT:PLAINTEXT,INTERNAL_PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL" \
    -e KAFKA_INTER_BROKER_LISTENER_NAME="INTERNAL_PLAINTEXT" \
    -e KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://broker10.jarry.dk:9092,INTERNAL_PLAINTEXT://:9094" \
    -e KAFKA_BROKER_ID="10" \
    -e KAFKA_LOG_DIRS="/var/kafka/data" \
    -v kafka-server-broker10-data:/var/kafka/data \
    docker.io/jarrydk/fedora-minimal-kafka:3.5.0