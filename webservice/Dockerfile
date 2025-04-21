FROM openjdk:21-jdk-alpine

RUN mkdir -p /app
WORKDIR /app

ARG WAR_FILE=webservice/target/tausecure-webservice*.jar
COPY ${WAR_FILE} tausecure-webservice.jar
COPY tau_secure_start.sh tau_secure_start.sh

ENTRYPOINT ["sh", "/app/tau_secure_start.sh"]