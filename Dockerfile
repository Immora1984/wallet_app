FROM bellsoft/liberica-openjre-alpine:17

COPY target/bankcards.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]