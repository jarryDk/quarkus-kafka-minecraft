#!/bin/bash

FORGE_SERVER_VERSION=1.19.3-44.1.23
FORGE_SERVER_LOCATION="/opt/minecraft/forge/.minecraft_$FORGE_SERVER_VERSION"

FORGE_MOD_FOLDER="$FORGE_SERVER_LOCATION/mods"

if [ ! -d $FORGE_MOD_FOLDER ]; then
    mkdir -p $FORGE_MOD_FOLDER
    echo "Forge folder created -> $FORGE_MOD_FOLDER"
else
    echo "Forge folder was present -> $FORGE_MOD_FOLDER"
fi

if [ ! -f $FORGE_MOD_FOLDER/kafka-clients-3.4.0.jar ]; then
    cp -v libs/kafka-clients-3.4.0.jar $FORGE_MOD_FOLDER/kafka-clients-3.4.0.jar
    cp -v libs/zstd-jni-1.5.2-1.jar $FORGE_MOD_FOLDER/zstd-jni-1.5.2-1.jar
    cp -v libs/lz4-java-1.8.0.jar $FORGE_MOD_FOLDER/lz4-java-1.8.0.jar
    cp -v libs/snappy-java-1.1.8.4.jar $FORGE_MOD_FOLDER/snappy-java-1.1.8.4.jar

    cp -v libs/jackson-annotations-2.14.2.jar $FORGE_MOD_FOLDER/jackson-annotations-2.14.2.jar
    cp -v libs/jackson-core-2.14.2.jar $FORGE_MOD_FOLDER/jackson-core-2.14.2.jar
    cp -v libs/jackson-databind-2.14.2.jar $FORGE_MOD_FOLDER/jackson-databind-2.14.2.jar
else
    echo "kafka-clients-3.4.0.jar was in the folder : $FORGE_MOD_FOLDER"
fi

UNIX_ARGS_FILE="$FORGE_SERVER_LOCATION/libraries/net/minecraftforge/forge/$FORGE_SERVER_VERSION/unix_args.txt"

if [ "X" == "X$(grep kafka-clients-3.4.0.jar $UNIX_ARGS_FILE)" ]; then
    echo "GO"
    search="-DlegacyClassPath="
    replace="-DlegacyClassPath=mods\/kafka-clients-3.4.0.jar:mods\/lz4-java-1.8.0.jar:mods\/snappy-java-1.1.8.4.jar:mods\/zstd-jni-1.5.2-1.jar:mods\/jackson-databind-2.14.2.jar:mods\/jackson-annotations-2.14.2.jar:mods\/jackson-core-2.14.2.jar:"
    sed -i -e "s/"$search"/"$replace"/g" $UNIX_ARGS_FILE
else
    echo "NoGo"
fi

cp -v build/libs/kafka-0.2.jar $FORGE_MOD_FOLDER/kafka-0.2.jar