FROM maven AS build-stage

WORKDIR /workspace
COPY ./src /workspace/src
COPY ./pom.xml /workspace/

RUN mvn package && cp target/spring-2021-1.0-SNAPSHOT-jar-with-dependencies.jar codingame-spring-2021.jar
