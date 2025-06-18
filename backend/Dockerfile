# --- FASE 1: Build con Maven ---
# Usiamo un'immagine che contiene sia Java che Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia prima il pom.xml per sfruttare la cache di Docker
COPY backend/pom.xml .

# Scarica le dipendenze (questo layer verrà messo in cache)
# L'opzione -B (batch mode) è utile per gli script
RUN mvn -B dependency:go-offline

COPY backend/src ./src 


# -DskipTests salta l'esecuzione dei test per una build più veloce
RUN mvn -B package -DskipTests

# --- FASE 2: Esecuzione ---

FROM eclipse-temurin:21-alpine as jre-build

WORKDIR /app

COPY --from=build /app/target/grafosupporter-0.0.1-SNAPSHOT.jar app.jar
COPY backend/uploads ./uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]