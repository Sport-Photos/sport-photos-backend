FROM adoptopenjdk:11.0.11_9-jdk-hotspot AS gradle-build
WORKDIR /buildtmp
COPY . ./
RUN chmod 777 gradlew
RUN ./gradlew clean spotlessApply build -x test --info --stacktrace

FROM openjdk:11.0.11-jre-slim
COPY --from=gradle-build /buildtmp/build/libs/sport-photos-backend-1.0-SNAPSHOT.jar /usr/app/sport-photos-backend-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/sport-photos-backend-1.0-SNAPSHOT.jar"]