FROM maven:3.6.3-openjdk-17-slim as BUILDER
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src

RUN mvn clean package -DskipTests
COPY target/demo-${VERSION}.jar target/application.jar

FROM openjdk:17.0.2-slim
WORKDIR /app/

COPY --from=BUILDER /build/target/application.jar /app/
CMD java -jar /app/application.jar