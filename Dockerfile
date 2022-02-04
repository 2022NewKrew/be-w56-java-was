FROM eclipse-temurin:11
#FROM ubuntu
RUN mkdir /opt/app
COPY ./build/libs/java-was-1.0-SNAPSHOT-app.jar /opt/app
COPY ./webapp/ /opt/app/webapp
EXPOSE 8080
WORKDIR /opt/app
CMD ["java", "-jar", "java-was-1.0-SNAPSHOT-app.jar"]
