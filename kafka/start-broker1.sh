#!/bin/bash

KAFKA_ZOOKEEPER_CONNECT="zookeeper.jarry.dk:2181"
KAFKA_LISTENERS="PLAINTEXT://:9192,SSL://:9193,INTERNAL_PLAINTEXT://:9194"
KAFKA_LISTENER_SECURITY_PROTOCAL_MAP="PLAINTEXT:PLAINTEXT,INTERNAL_PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL"
KAFKA_INTER_BROKER_LISTENER_NAME="INTERNAL_PLAINTEXT"

KAFKA_SSL_TRUSTSTORE_LOCATION="/opt/apache/kafka/jarry_dk/kafka.server.truststore.jks"
KAFKA_SSL_TRUSTSTORE_PASSWORD="password1234"

KAFKA_SSL_KEYSTORE_LOCATION="/opt/apache/kafka/jarry_dk/broker1.jarry.dk.keystore.jks"
KAFKA_SSL_KEYSTORE_PASSWORD="password1234"

KAFKA_SSL_CLIENT_AUTH="requested"

KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://broker1.jarry.dk:9192,SSL://broker1.jarry.dk:9193,INTERNAL_PLAINTEXT://broker1.jarry.dk:9194"
KAFKA_BROKER_ID="1"
KAFKA_LOG_DIRS="/tmp/kafka-logs-broker-1"

$KAFKA_HOME/bin/kafka-server-start.sh \
	$KAFKA_HOME/config/server.properties \
	--override zookeeper.connect=$KAFKA_ZOOKEEPER_CONNECT \
    --override listeners=$KAFKA_LISTENERS \
    --override listener.security.protocol.map=$KAFKA_LISTENER_SECURITY_PROTOCAL_MAP \
    --override inter.broker.listener.name=$KAFKA_INTER_BROKER_LISTENER_NAME \
	--override ssl.truststore.location=$KAFKA_SSL_TRUSTSTORE_LOCATION \
	--override ssl.truststore.password=$KAFKA_SSL_TRUSTSTORE_PASSWORD \
    --override ssl.keystore.location=$KAFKA_SSL_KEYSTORE_LOCATION \
	--override ssl.keystore.password=$KAFKA_SSL_KEYSTORE_PASSWORD \
    --override ssl.client.auth=$KAFKA_SSL_CLIENT_AUTH \
    --override advertised.listeners=$KAFKA_ADVERTISED_LISTENERS \
    --override broker.id=$KAFKA_BROKER_ID \
    --override log.dirs=$KAFKA_LOG_DIRS