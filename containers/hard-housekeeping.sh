#!/bin/bash

podman rm minecraft-kafka_kafka-zookeeper_1
podman rm minecraft-kafka_kafka-server-broker1_1
podman rm minecraft-kafka_kafka-server-broker2_1
podman rm minecraft-kafka_kafka-server-broker3_1

podman volume rm minecraft-kafka_kafka-server-broker1-data
podman volume rm minecraft-kafka_kafka-server-broker2-data
podman volume rm minecraft-kafka_kafka-server-broker3-data