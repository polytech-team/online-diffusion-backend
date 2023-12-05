FROM eclipse-temurin:17
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src

WORKDIR /opt/app
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker,main, example, photomock"]
