#!/bin/bash

podman volume create minecraft-kafka_kafka-ssl
podman volume import minecraft-kafka_kafka-ssl ../ssl/kafka-ssl.tar
