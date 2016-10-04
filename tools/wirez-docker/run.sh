#!/bin/sh

docker run -p 8080:8080 -p 8001:8001 -t -i --name stunner-showcase jboss/stunner-showcase:latest
