#!/bin/bash

NAME=minecraft-kafka

podman stop $NAME"_kafka-zookeeper_1"
podman stop $NAME"_kafka-server-broker1_1"
podman stop $NAME"_kafka-server-broker2_1"
podman stop $NAME"_kafka-server-broker3_1"

podman rm $NAME"_kafka-zookeeper_1"
podman rm $NAME"_kafka-server-broker1_1"
podman rm $NAME"_kafka-server-broker2_1"
podman rm $NAME"_kafka-server-broker3_1"

podman volume rm $NAME"_kafka-zookeeper"
podman volume rm $NAME"_kafka-server-broker1-data"
podman volume rm $NAME"_kafka-server-broker2-data"
podman volume rm $NAME"_kafka-server-broker3-data"

