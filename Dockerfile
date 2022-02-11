FROM adoptopenjdk/openjdk11
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENV MONGODB_HOST mongodb
ENTRYPOINT ["java", "-jar", "app.jar"]
