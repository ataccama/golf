FROM juriad/base

ENV JDBC_CONNECTION= \
	JDBC_USERNAME= \
	JDBC_PASSWORD= \
	RABBIT_HOST= \
	RABBIT_PORT=5672 \
	RABBIT_USERNAME= \
	RABBIT_PASSWORD=

ADD build/libs/app-1.0.0.jar /app.jar

CMD PGCONN=$(echo "${JDBC_CONNECTION}" | sed -E 's#[^/]*//([^/]*)/.*#\1#') && \
    ./wait-for-it.sh -t 60 "$PGCONN" -- ./wait-for-it.sh "$RABBIT_HOST:$RABBIT_PORT" -- ./start.sh
  