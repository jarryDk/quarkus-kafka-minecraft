#!/bin/bash

podman stop containers_kafka-zookeeper_1
podman stop containers_kafka-server-broker1_1
podman stop containers_kafka-server-broker2_1

podman rm containers_kafka-zookeeper_1
podman rm containers_kafka-server-broker1_1
podman rm containers_kafka-server-broker2_1

podman volume rm containers_kafka-zookeeper
podman volume rm containers_kafka-server-broker1-data
podman volume rm containers_kafka-server-broker2-data

