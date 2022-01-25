FROM openjdk:11
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ["./gradlew", "--watch-fs", "-Dorg.gradle.vfs.verbose=true", "build"]
CMD ["java", "-jar", "./build/libs/java-was-1.0-SNAPSHOT.jar"]
