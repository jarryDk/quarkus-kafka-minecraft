#!/bin/bash

podman volume create minecraft-kafka_kafka-ssl
podman import ../ssl/kafka-ssl.tar minecraft-kafka_kafka-ssl
