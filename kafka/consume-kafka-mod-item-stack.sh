#!/bin/bash

$KAFKA_HOME/bin/kafka-console-consumer.sh \
    --bootstrap-server broker1.jarry.dk:9192 \
    --topic kafka-mod-item-stack \
    --from-beginning | jq