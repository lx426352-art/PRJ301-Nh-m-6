# AITA System - Group 5 Docker Sandbox Container
# Builds WAR file with Maven and runs inside Apache Tomcat 9 (Java 17/8)

FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17-temurin
LABEL maintainer="AITA System Group 5 - Git Analytics Assessor"
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/target/aita-git-analytics.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
