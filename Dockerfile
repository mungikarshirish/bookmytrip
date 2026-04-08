FROM public.ecr.aws/docker/library/eclipse-temurin:21-jdk
# Create non-root user
RUN useradd -m bookmytrip
# Set working directory
WORKDIR /app
# Copy jar file
COPY target/bookmytrip*.jar app.jar
# Change ownership to new user
RUN chown -R bookmytrip:bookmytrip /app
# Switch to non-root user
USER bookmytrip
# Expose port
EXPOSE 8080
# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]