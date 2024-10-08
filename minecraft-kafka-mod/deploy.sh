#!/bin/bash

FORGE_SERVER_VERSION=${VERSION_MINECRAFT_FORGE_SERVER:=1.21.1-52.0.9}
FORGE_SERVER_LOCATION="/opt/minecraft/forge/.minecraft_$FORGE_SERVER_VERSION"

FORGE_MOD_FOLDER="$FORGE_SERVER_LOCATION/mods"

if [ ! -d $FORGE_MOD_FOLDER ]; then
    mkdir -p $FORGE_MOD_FOLDER
    echo "Forge folder created -> $FORGE_MOD_FOLDER"
else
    echo "Forge folder was present -> $FORGE_MOD_FOLDER"
fi

cp -v build/libs/kafka-1.0.6.jar $FORGE_MOD_FOLDER/kafka-1.0.6.jar