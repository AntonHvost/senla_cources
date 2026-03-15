FROM tomcat:11.0-jdk21-temurin
LABEL authors="novik"

WORKDIR /usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/*

COPY app/target/*.war /usr/local/tomcat/webapps/my-app.war

EXPOSE 8080

CMD ["catalina.sh", "run"]