#!/bin/bash

# *****************************************************
# Stunner Showcase - Docker image build script
# *****************************************************

WAR_SOURCE_PATH=../wirez-distros/target
WAR_TARGET_PATH=./bin
WAR_FILE_NAME=wirez-0.5.0-SNAPSHOT-wildfly10.war
WAR_SOURCE_LOCATION="$WAR_SOURCE_PATH/$WAR_FILE_NAME"
WAR_TARGET_LOCATION="$WAR_TARGET_PATH/$WAR_FILE_NAME"
IMAGE_NAME="jboss/stunner-showcase"
IMAGE_TAG="latest"

# Remove the current war, if any.
if [ -f $WAR_TARGET_LOCATION ]; then
    rm -f $WAR_TARGET_LOCATION
fi

# Copy the war for using adding it on the docker image.
cp -f $WAR_SOURCE_LOCATION $WAR_TARGET_PATH

# Remove current image, if any.
docker rmi $IMAGE_NAME:$IMAGE_TAG

# Build the container image.
echo "Building the Docker container for $IMAGE_NAME:$IMAGE_TAG.."
docker build --rm -t $IMAGE_NAME:$IMAGE_TAG .
echo "Build done"
