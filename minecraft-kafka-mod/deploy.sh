#!/bin/bash

FORGE_MOD_FOLDER="/opt/minecraft/forge/.minecraft/mods"

if [ ! -d $FORGE_MOD_FOLDER ]; then
    mkdir -p $FORGE_MOD_FOLDER
    echo "Forge folder created -> $FORGE_MOD_FOLDER"
else
    echo "Forge folder was present -> $FORGE_MOD_FOLDER"
fi

if [ ! -f $FORGE_MOD_FOLDER/kafka-clients-3.2.1.jar ]; then
    cp -v libs/kafka-clients-3.2.1.jar $FORGE_MOD_FOLDER/kafka-clients-3.2.1.jar
    cp -v libs/lz4-java-1.8.0.jar $FORGE_MOD_FOLDER/lz4-java-1.8.0.jar
    cp -v libs/jackson-annotations-2.13.3.jar $FORGE_MOD_FOLDER/jackson-annotations-2.13.3.jar
    cp -v libs/snappy-java-1.1.8.4.jar $FORGE_MOD_FOLDER/snappy-java-1.1.8.4.jar
    cp -v libs/jackson-core-2.13.3.jar $FORGE_MOD_FOLDER/jackson-core-2.13.3.jar
    cp -v libs/zstd-jni-1.5.2-1.jar $FORGE_MOD_FOLDER/zstd-jni-1.5.2-1.jar
    cp -v libs/jackson-databind-2.13.3.jar $FORGE_MOD_FOLDER/jackson-databind-2.13.3.jar
else
    echo "kafka-clients-3.2.1.jar was in the folder : $FORGE_MOD_FOLDER"
fi

cp -v build/libs/kafka-0.1.jar $FORGE_MOD_FOLDER/kafka-0.1.jar