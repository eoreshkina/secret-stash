FROM amazoncorretto:21-alpine

ENV SPRING_PROFILES_ACTIVE=local

WORKDIR /app
COPY build/libs/*.jar notes.jar
EXPOSE 8080
ENTRYPOINT ["java",  "-jar", "/app/notes.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]