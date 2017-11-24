FROM openjdk:8-jdk
LABEL maintainer="<juriad@gmail.com>"

RUN apt-get update && apt-get install -y \
	ca-certificates \
	bash \
	curl \
	netcat \
	vim \
	postgresql-client

ENV DEBUG_PORT= \
	DEBUG_SUSPEND=n

ADD *.sh /
RUN chmod +x /*.sh
