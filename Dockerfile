FROM maven AS build-stage

WORKDIR /workspace
COPY ./src /workspace/src
COPY ./pom.xml /workspace/
COPY ./assembly.xml /workspace/

RUN mvn assembly:assembly -Ddescriptor=assembly.xml

FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.1.13

WORKDIR /app

RUN apt update -y && apt upgrade -y \
    && apt-get install -y \
    python3-pip \
    && apt-get clean

COPY --from=build-stage /workspace/target/spring-2021-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
COPY ./config /app/config

CMD [ "java", "-cp", "app.jar", "Spring2021" ]
