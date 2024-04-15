FROM openjdk:17
COPY target/config-service-1.0.0.jar /app/config.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=k8s", "/app/config.jar"]
