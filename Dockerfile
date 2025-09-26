# Use official OpenJDK 21 base image
FROM openjdk:21-jdk-slim

# Install CA certificates for SSL
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Add a build argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR into the container
COPY ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Force TLS 1.2/1.3 and enable SSL handshake debug
ENV JAVA_OPTS="-Dhttps.protocols=TLSv1.2,TLSv1.3 -Djavax.net.debug=ssl,handshake,keymanager,trustmanager"

# Optional: print JAVA_OPTS and run the app
ENTRYPOINT ["sh", "-c", "echo 'JAVA_OPTS='$JAVA_OPTS; java $JAVA_OPTS -jar app.jar"]
