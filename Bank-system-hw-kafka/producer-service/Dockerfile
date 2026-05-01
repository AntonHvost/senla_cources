FROM maven:3.9-sapmachine-21 AS builder
WORKDIR /build
LABEL authors="novik"

COPY pom.xml /build/pom.xml
COPY consumer-service/pom.xml /build/consumer-service/pom.xml
COPY producer-service/pom.xml /build/producer-service/pom.xml
COPY producer-service/src /build/producer-service/src

RUN mvn -f /build/pom.xml -pl producer-service -am clean package -DskipTests -q

FROM sapmachine:21-jre-alpine-3.23
RUN addgroup -S appgroup && adduser -S -g appgroup appuser

WORKDIR /app
COPY --from=builder /build/producer-service/target/producer-service.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"

USER appuser
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]