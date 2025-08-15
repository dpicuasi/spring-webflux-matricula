FROM eclipse-temurin:21

MAINTAINER mitocode.com

COPY target/spring-matricula-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]