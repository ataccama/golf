FROM juriad/base

ENV RABBIT_HOST= \
	RABBIT_PORT=5672 \
	RABBIT_USERNAME= \
	RABBIT_PASSWORD=

ADD build/libs/app-1.0.0.jar /app.jar

CMD ./wait-for-it.sh -t 60 "$RABBIT_HOST:$RABBIT_PORT" -- ./start.sh
