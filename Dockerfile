FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src

RUN ./mvnw -q -DskipTests package

EXPOSE 8080

ENTRYPOINT ["java","-jar","target/backend-senior-seplag-0.0.1-SNAPSHOT.jar"]
