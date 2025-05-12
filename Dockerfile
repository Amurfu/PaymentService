FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/*.jar
ENV TZ=Etc/UTC
WORKDIR /app
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","/app/app.jar"]
