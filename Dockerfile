# Imagen base con Java 21
FROM eclipse-temurin:21-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR generado por Maven (compilado por Jenkins)
COPY target/server-gateway-*.jar app.jar

# Variable de entorno (puedes sobrescribirla en docker run o docker-compose)
ENV SERVER_DISCOVERY="http://host.docker.internal:8761/eureka"

# Puerto del microservicio
EXPOSE 9090

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]