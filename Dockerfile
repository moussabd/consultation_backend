# =========================
# 1️⃣ Stage BUILD (Maven)
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copier les fichiers nécessaires au build
COPY pom.xml .
COPY src ./src

# Construire le JAR (skip tests si besoin)
RUN mvn clean package -DskipTests

# =========================
# 2️⃣ Stage RUN (JRE léger)
# =========================
FROM eclipse-temurin:21-jre-alpine

# Sécurité : utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copier le JAR généré depuis le stage build
COPY --from=build /build/target/*.jar app.jar

EXPOSE 10001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
