#!/bin/bash

DRY_RUN=false

KAFKA_TOPIC_KAFKA_MOD_CHAT="kafka-mod-chat"
KAFKA_TOPIC_KAFKA_MOD_ENTITY_EVENT="kafka-mod-entity-event"
KAFKA_TOPIC_KAFKA_MOD_ITEM_STACK="kafka-mod-item-stack"

KAFKA_HOST=localhost
KAFKA_PORT=9092
KAFKA_TOPIC=

while [[ $# -gt 1 ]]; do
    case $1 in
        -t|--topic)
            KAFKA_TOPIC="$2"
            shift # past argument
            shift # past value
            ;;
        -h|--host)
            KAFKA_HOST="$2"
            shift # past argument
            shift # past value
            ;;
        -p|--port)
            KAFKA_PORT="$2"
            shift # past argument
            shift # past value
            ;;
        --dryrun)
            DRY_RUN="$2"
            shift # past argument
            shift # past value
            ;;
        -*|--*)
            echo "Unknown option $1"
            exit 1
            ;;
    esac
shift # past argument or value
done

if [ $DRY_RUN = true ]; then
    echo ""
	echo "Topic: 	$KAFKA_TOPIC"
	echo "Host: 	$KAFKA_HOST"
	echo "Port: 	$KAFKA_PORT"
    echo "Dryrun - Will not listen to topic"
    exit 0
fi

if [ ! -z $KAFKA_TOPIC ]; then
	printf "\e]2;Topic: $KAFKA_TOPIC - Host: $KAFKA_HOST - Port: $KAFKA_PORT\a"

	$KAFKA_HOME/bin/kafka-console-consumer.sh \
		--bootstrap-server $KAFKA_HOST:$KAFKA_PORT \
		--topic $KAFKA_TOPIC \
		--from-beginning | jq
else
	echo "Usage:"
	echo "	./consume_topics.sh [opstions]"
	echo ""
    echo "Options:"
    echo "  -t|--topic      Topi: $KAFKA_TOPIC_KAFKA_MOD_CHAT | $KAFKA_TOPIC_KAFKA_MOD_ENTITY_EVENT | $KAFKA_TOPIC_KAFKA_MOD_ITEM_STACK"
    echo "  -h | --host     Host (default \"$KAFKA_HOST\")"
    echo "  -p | --port     Port (default \"$KAFKA_PORT\")"
    echo "  --dryrun        Dryrun (true|false) (default \"$DRY_RUN\")"
    echo ""
    echo "sample:"
    echo "  ./consume_topics.sh -t kafka-mod-chat"
fi
