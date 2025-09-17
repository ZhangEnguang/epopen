FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ADD target/ai-open-backend-0.0.1-SNAPSHOT.jar .

EXPOSE 12138

ENTRYPOINT ["java","-jar","/app/ai-open-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]