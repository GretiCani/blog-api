FROM amazoncorretto:21
WORKDIR /app
COPY target/blog-api-0.0.1-SNAPSHOT.jar /app/blog-api.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/blog-api.jar"]
