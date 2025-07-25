# Imagen base con JDK 21 para ejecutar el JAR
FROM eclipse-temurin:21-alpine
WORKDIR /app

# Argumento para pasar el nombre del JAR en tiempo de build
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# Puerto de la aplicación
EXPOSE 9090

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]