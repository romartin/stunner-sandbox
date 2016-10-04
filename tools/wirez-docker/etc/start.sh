#!/usr/bin/env bash

# Start Wildfly with the given arguments.
echo "Running Stunner on JBoss Wildfly 10 ..."
exec ./standalone.sh -b $JBOSS_BIND_ADDRESS -c $KIE_SERVER_PROFILE.xml
exit $?