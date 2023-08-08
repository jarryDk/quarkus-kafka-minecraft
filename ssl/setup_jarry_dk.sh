#!/bin/bash

set -euo pipefail

cd ${0%/*}

if [ -z ${KAFKA_JARRY_DK_STOREPASS+x} ]; then
    echo "KAFKA_JARRY_DK_STOREPASS need to be set via export"
    echo "export KAFKA_JARRY_DK_STOREPASS=password1234"
    exit 0
fi

if [ -z ${KAFKA_JARRY_DK_KEYPASS+x} ]; then
    echo "KAFKA_JARRY_DK_KEYPASS need to be set via export"
    echo "export KAFKA_JARRY_DK_KEYPASS=password1234"
    exit 0
fi

echo "Creating CA Authority"
./create_ca_authority.sh -name Jarry

OUTPUT_FOLDER=/opt/apache/kafka/jarry_dk

mkdir -p $OUTPUT_FOLDER

echo "Creating server certificates for broker1.jarry.dk"
./create_server_certificates.sh -name broker1.jarry.dk -spass $KAFKA_JARRY_DK_STOREPASS -kpass $KAFKA_JARRY_DK_KEYPASS
cp -v server_certs/broker1.jarry.dk.keystore.jks $OUTPUT_FOLDER/broker1.jarry.dk.keystore.jks

echo "Creating server certificates for broker2.jarry.dk"
./create_server_certificates.sh -name broker2.jarry.dk -spass $KAFKA_JARRY_DK_STOREPASS -kpass $KAFKA_JARRY_DK_KEYPASS
cp -v server_certs/broker2.jarry.dk.keystore.jks $OUTPUT_FOLDER/broker2.jarry.dk.keystore.jks

echo "Creating server certificates for broker3.jarry.dk"
./create_server_certificates.sh -name broker3.jarry.dk -spass $KAFKA_JARRY_DK_STOREPASS -kpass $KAFKA_JARRY_DK_KEYPASS
cp -v server_certs/broker3.jarry.dk.keystore.jks $OUTPUT_FOLDER/broker3.jarry.dk.keystore.jks

echo "Move kafka.server.truststore.jks"
cp -v server_certs/kafka.server.truststore.jks $OUTPUT_FOLDER/kafka.server.truststore.jks

echo "Creating client certificates for jarry.dk"
./create_client_certificates.sh
cp -v client_certs/kafka.client.truststore.jks $OUTPUT_FOLDER/kafka.client.truststore.jks