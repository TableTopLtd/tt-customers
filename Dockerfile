FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./api/target/places-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "places-api-1.0.0-SNAPSHOT.jar"]
