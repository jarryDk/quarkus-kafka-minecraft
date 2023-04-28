#!/bin/bash

echo "----------------------------------------"
echo "- Getting ready to download dependencies"
echo "----------------------------------------"

mkdir -p libs

#
# kafka-clients and dependensies
#
if [ ! -f libs/kafka-clients-3.4.0.jar ]; then
    curl https://repo1.maven.org/maven2/org/apache/kafka/kafka-clients/3.4.0/kafka-clients-3.4.0.jar -o libs/kafka-clients-3.4.0.jar
else
    echo "We already have the file kafka-clients-3.4.0.jar"
fi
if [ ! -f libs/zstd-jni-1.5.2-1.jar ]; then
    curl https://repo1.maven.org/maven2/com/github/luben/zstd-jni/1.5.2-1/zstd-jni-1.5.2-1.jar -o libs/zstd-jni-1.5.2-1.jar
else
    echo "We already have the file zstd-jni-1.5.2-1.jar"
fi
if [ ! -f libs/lz4-java-1.8.0.jar ]; then
    curl https://repo1.maven.org/maven2/org/lz4/lz4-java/1.8.0/lz4-java-1.8.0.jar -o libs/lz4-java-1.8.0.jar
else
    echo "We already have the file lz4-java-1.8.0.jar"
fi
if [ ! -f libs/snappy-java-1.1.8.4.jar ]; then
    curl https://repo1.maven.org/maven2/org/xerial/snappy/snappy-java/1.1.8.4/snappy-java-1.1.8.4.jar -o libs/snappy-java-1.1.8.4.jar
else
    echo "We already have the file snappy-java-1.1.8.4.jar"
fi

#
# jackson-databind and dependensies
#
if [ ! -f libs/jackson-databind-2.14.2.jar ]; then
    curl https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.jar -o libs/jackson-databind-2.14.2.jar
else
    echo "We already have the file jackson-databind-2.14.2.jar"
fi
if [ ! -f libs/jackson-annotations-2.14.2.jar ]; then
    curl https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.jar -o libs/jackson-annotations-2.14.2.jar
else
    echo "We already have the file jackson-annotations-2.14.2.jar"
fi
if [ ! -f libs/jackson-core-2.14.2.jar ]; then
    curl https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.jar -o libs/jackson-core-2.14.2.jar
else
    echo "We already have the file jackson-core-2.14.2.jar"
fi
