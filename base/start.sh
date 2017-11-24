#!/bin/bash

if [ -z ${DEBUG_PORT} ]; then
	echo "Starting" && java ${JAVA_OPTS} -jar /app.jar;
else \
	echo "Starting in debug mode" && java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=${DEBUG_PORT},suspend=${DEBUG_SUSPEND} ${JAVA_OPTS} -jar /app.jar;
fi