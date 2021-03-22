FROM maven:3.6.3-adoptopenjdk-11 AS maven-build
WORKDIR /build
COPY . ./
RUN mvn clean package -DskipTests

FROM openjdk:11.0.10-jre-slim
COPY --from=maven-build /build/target/sport-photos-backend-1.0-SNAPSHOT.jar /usr/app/sport-photos-backend-1.0-SNAPSHOT.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/usr/app/sport-photos-backend-1.0-SNAPSHOT.jar"]