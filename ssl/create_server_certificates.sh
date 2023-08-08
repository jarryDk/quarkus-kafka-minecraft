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

if [ -f $KEY_STORE_PATH ]; then
    echo "File $KEY_STORE_PATH already existed"
    echo "Please delete the file $KEY_STORE_PATH before running this script"
    exit 0
fi

mkdir -p $SERVER_CERTS_FOLDER

echo "Create Kafka Server Certificate and store in KeyStore - dname : CN=$SERVER_DNAME"
keytool -genkey \
    -keystore $KEY_STORE_PATH \
    -validity $SERVER_VALIDITY \
    -dname CN=$SERVER_DNAME \
    -keyalg RSA \
    -storetype pkcs12 \
    -storepass $KAFKA_STOREPASS \
    -keypass $KAFKA_KEYPASS

echo "Create Certificate signed request (CSR)"
keytool -keystore $KEY_STORE_PATH \
    -certreq \
    -file $SERVER_CERTS_FOLDER/cert-file \
    -storepass $KAFKA_STOREPASS \
    -keypass $KAFKA_KEYPASS

echo "Get CSR Signed with the CA"
openssl x509 -req \
    -CA $CA_CERT_PATH \
    -CAkey $CA_KEY_PATH \
    -in $SERVER_CERTS_FOLDER/cert-file \
    -out $SERVER_CERTS_FOLDER/cert-file-signed \
    -days $SERVER_VALIDITY \
    -CAcreateserial \
    -passin pass:$CA_PASS

echo "Import CA certificate in KeyStore"
keytool -keystore $KEY_STORE_PATH \
    -alias CARoot \
    -import \
    -file $CA_CERT_PATH \
    -storepass $KAFKA_STOREPASS \
    -keypass $KAFKA_KEYPASS \
    -noprompt

echo "Import Signed CSR In KeyStore"
keytool -keystore $KEY_STORE_PATH \
    -import \
    -file $SERVER_CERTS_FOLDER/cert-file-signed \
    -storepass $KAFKA_STOREPASS \
    -keypass $KAFKA_KEYPASS \
    -noprompt

if [ ! -f $TRUST_STORE_PATH ]; then
    echo "Import CA certificate In TrustStore"
    keytool -keystore $TRUST_STORE_PATH \
        -alias CARoot \
        -import \
        -file $CA_CERT_PATH \
        -storepass $KAFKA_STOREPASS \
        -keypass $KAFKA_KEYPASS \
        -noprompt
else
    echo "We already have $TRUST_STORE_PATH"
fi

echo "Housekeeping"
rm -v $SERVER_CERTS_FOLDER/cert-file
rm -v $SERVER_CERTS_FOLDER/cert-file-signed
