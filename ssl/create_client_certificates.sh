#!/bin/bash

set -euo pipefail
cd ${0%/*}

source ./config.conf

while [[ $# -gt 1 ]]
do
key="$1"
case $key in
    -name|--dname)
    SERVER_DNAME="$2"
    shift # past argument
    ;;
    -v|--validity)
    SERVER_VALIDITY="$2"
    shift # past argument
    ;;
    -spass|--storepassword)
    KAFKA_STOREPASS="$2"
    shift # past argument
    ;;
    -kpass|--keypassword)
    KAFKA_KEYPASS="$2"
    shift # past argument
    ;;
    --default)
    DEFAULT=YES
    ;;
    *)
    # unknown option
    ;;
esac
shift # past argument or value
done

KEY_STORE_PATH="$SERVER_CERTS_FOLDER/$SERVER_DNAME.keystore.jks"
TRUST_STORE_PATH="$SERVER_CERTS_FOLDER/kafka.server.truststore.jks"

if [ ! -d $CA_FOLDER ]; then
    echo "The folder $CA_FOLDER is missing"
    echo "Please run ./create_ca_authority.sh before running this script"
    exit 0
fi

mkdir -p $CLIENT_CERTS_FOLDER

echo "Import CA certificate In TrustStore:"
keytool -keystore $CLIENT_CERTS_FOLDER/kafka.client.truststore.jks \
    -alias CARoot \
    -import \
    -file $CA_FOLDER/ca-cert \
    -storepass $KAFKA_STOREPASS \
    -keypass $KAFKA_KEYPASS \
    -noprompt