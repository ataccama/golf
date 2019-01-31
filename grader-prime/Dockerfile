FROM juriad/base:1.0

ENV RABBIT_HOST= \
	RABBIT_PORT=5672 \
	RABBIT_USERNAME= \
	RABBIT_PASSWORD=

ADD build/libs/app-1.0.0.jar /app.jar
ADD huge.txt /huge.txt

CMD ./wait-for-it.sh -t 60 "$RABBIT_HOST:$RABBIT_PORT" -- ./start.sh
