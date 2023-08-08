#!/bin/bash

set -euo pipefail
cd ${0%/*}

source ./config.conf

mkdir -p $CA_FOLDER

while [[ $# -gt 1 ]]
do
key="$1"
case $key in
    -name)
    CA_AUTHORITY_CN="$2"
    shift # past argument
    ;;
    -v|--validity)
    CA_VALIDITY="$2"
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

openssl req -new \
    -newkey rsa:4096 \
    -days $CA_VALIDITY \
    -x509 \
    -subj /CN=$CA_AUTHORITY_CN \
    -keyout $CA_KEY_PATH \
    -out $CA_CERT_PATH \
    -nodes

echo "Certificate Authority (CA) created with CN : $CA_AUTHORITY_CN"