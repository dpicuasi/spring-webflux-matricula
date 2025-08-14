FROM eclipse-temurin:21

MAINTAINER mitocode.com

COPY target/spring-reactor-0.0.1-SNAPSHOT.jar reactor-0.0.1.jar

ENTRYPOINT ["java","-jar","/reactor-0.0.1.jar"]