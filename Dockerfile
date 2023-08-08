FROM openjdk:17-alpine
ARG JAR_FILE=target/postgres-fts-demo-*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
