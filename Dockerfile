# ============================================
# STAGE 1: Build
# ============================================
FROM eclipse-temurin:21.0.3_9-jdk AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY ./pom.xml .
COPY ./.mvn ./.mvn
COPY ./mvnw .

# Dar permisos de ejecución a mvnw
RUN chmod +x ./mvnw

# Descargar dependencias (capa cacheada)
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY ./src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# ============================================
# STAGE 2: Runtime (Alpine - mucho más ligero)
# ============================================
FROM eclipse-temurin:21.0.3_9-jre-alpine

# Crear usuario no-root para ejecutar la aplicación
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copiar solo el JAR desde el stage de build
COPY --from=builder /app/target/hexagonal-architecture-example-0.0.1-SNAPSHOT.jar app.jar

# Eliminar cache
RUN rm -rf /tmp/*

# Cambiar propiedad de archivos al usuario no-root
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no-root
USER appuser

# Puerto de la aplicación
EXPOSE 8080

# Optimizaciones JVM para contenedores
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]