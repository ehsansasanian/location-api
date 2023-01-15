FROM maven:3.6.3-openjdk-17-slim AS BUILDER
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17.0.2-slim
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /
COPY --from=BUILDER /build/target/*.jar application.jar
EXPOSE 8080
CMD java -jar application.jar