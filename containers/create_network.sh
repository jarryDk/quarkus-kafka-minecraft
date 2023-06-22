#!/bin/bash

source ./config.conf

PODMAN_NETWORKS_LOOKUP=$(podman network ls --format json | jq  -r '[ .[].name ] | @csv' | tr -d '"')
IFS=',' read -r -a PODMAN_NETWORKS <<< "$PODMAN_NETWORKS_LOOKUP"

echo "podman networks :"
printf "+ %s\n"  "${PODMAN_NETWORKS[@]}"
echo

if [[ " ${PODMAN_NETWORKS[*]} " =~ "$PODMAN_NETWORK" ]]; then
    echo "We already have $PODMAN_NETWORK as a network"
else
    echo "We create $PODMAN_NETWORK as a network"
    podman network create $PODMAN_NETWORK
fi
