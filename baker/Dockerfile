FROM maven:3.6.3-jdk-8-slim

COPY ./stats-baker /stats-baker

WORKDIR /stats-baker

RUN mvn package

ENTRYPOINT /bin/bash run.sh
