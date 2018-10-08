FROM postgres:9.6

COPY *.sh /docker-entrypoint-initdb.d/
ENV POSTGRES_USER sky
ENV POSTGRES_PASSWORD sky

EXPOSE 5432
