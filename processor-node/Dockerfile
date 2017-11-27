FROM juriad/base

ENV RABBIT_HOST= \
	RABBIT_PORT=5672 \
	RABBIT_USERNAME= \
	RABBIT_PASSWORD= \
	DOCKER_HOST= \
	DOCKER_IMAGE= \
	DOCKER_TIMEOUT=

VOLUME /solutions
VOLUME /var/lib/docker

ADD build/libs/app-1.0.0.jar /app.jar

CMD ./wait-for-it.sh -t 60 "$RABBIT_HOST:$RABBIT_PORT" -- ./wait-for-it.sh "$DOCKER_HOST:2375" -- ./start.sh
