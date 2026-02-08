# ============================================
# DOCKERFILE - Multi-Stage Build
# ============================================
# STAGE 1: Compilación (pesado, herramientas de build)
# STAGE 2: Runtime (ligero, solo JRE)
# ============================================

# ============================================
# STAGE 1: Build - Compilar la aplicación
# ============================================
# Imagen base con JDK completo para compilar
FROM eclipse-temurin:21.0.3_9-jdk-alpine AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar configuración de Maven (estas capas se cachean)
COPY ./pom.xml .
COPY ./.mvn ./.mvn
COPY ./mvnw .

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw

# Descargar dependencias Maven (capa cacheada)
# Evita descargas repetidas en builds posteriores
RUN ./mvnw dependency:go-offline

# Copiar código fuente (cambios frecuentes, capa no cacheada)
COPY ./src ./src

# Compilar la aplicación
# -DskipTests: omite tests para acelerar build
RUN ./mvnw clean package -DskipTests


# ============================================
# STAGE 2: Runtime - Ejecutar la aplicación
# ============================================
# Imagen base con solo JRE (mucho más ligero que JDK)
FROM eclipse-temurin:21.0.3_9-jre-alpine

# Crear grupo de usuario (no-root = seguridad)
# -S: usuario de sistema (sin shell login)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Directorio de trabajo
WORKDIR /app

# Copiar JAR compilado desde STAGE 1
# --from=builder: referencia la capa anterior (Stage 1)
# --chown: asigna propiedad al usuario appuser en una sola capa
COPY --from=builder --chown=appuser:appgroup /app/target/hexagonalarchitecture-0.0.1-SNAPSHOT.jar app.jar

# Cambiar a usuario no-root para ejecutar la app, si la app es comprometida, daños limitados
USER appuser

# Exponer el puerto (solo documentación, no abre el puerto)
# El puerto se abre realmente en docker-compose.yml
EXPOSE 8080

# Comando de inicio
# Optimizaciones JVM para contenedores:
#   -XX:+UseContainerSupport: detecta límites de memoria del contenedor
#   -XX:MaxRAMPercentage=75.0: usa máximo 75% de la RAM disponible
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar"]