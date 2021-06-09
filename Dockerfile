FROM openjdk:8
LABEL maintainer="dinhhuy2808"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} boot-kafka-test-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","boot-kafka-test-0.0.1-SNAPSHOT.jar"]
