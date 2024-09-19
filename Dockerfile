# Build stage
FROM openjdk:17-alpine AS builder
WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew clean build --no-daemon -x test

FROM openjdk:17-alpine
WORKDIR /app

ENV TZ=Asia/Seoul
RUN apk update && apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN mkdir -p /app/logs

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]