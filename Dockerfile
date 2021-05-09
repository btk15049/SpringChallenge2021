FROM maven AS build-stage

WORKDIR /workspace
COPY ./src /workspace/src
COPY ./pom.xml /workspace/
COPY ./assembly.xml /workspace/

RUN mvn assembly:assembly -Ddescriptor=assembly.xml

FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.1.13

WORKDIR /app

RUN apt-get update -y && apt-get upgrade -y \
    && apt-get install -y software-properties-common \
    && apt-add-repository ppa:ubuntu-toolchain-r/test

RUN apt-get update -y && apt-get upgrade -y \
    && apt-get install -y \
    build-essential \
    g++-9 \
    libssl-dev \
    python3-pip \
    && apt-get clean

ENV CC gcc-9
ENV CXX g++-9
ENV CXXFLAGS -std=c++17

# setup g++
RUN update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 30
RUN update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-9 30

COPY --from=build-stage /workspace/target/spring-2021-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
COPY ./config /app/config

CMD [ "java", "-cp", "app.jar", "Spring2021" ]
