FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 4000
ARG JAR_FILE=target/zuul-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} zuul.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zuul.jar"]