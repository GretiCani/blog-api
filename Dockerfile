FROM amazoncorretto:21
WORKDIR /app
COPY /target/blog-api-0.0.1-SNAPSHOT.jar /app/blog-api.jar
COPY .env /app/.env
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
EXPOSE 8080
# Run the entrypoint script
ENTRYPOINT ["/app/entrypoint.sh"]

