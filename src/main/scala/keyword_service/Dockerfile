FROM adoptopenjdk/openjdk8:alpine-slim

COPY /src/resources/logback.xml logback.xml
COPY /target/scala-2.13/processing-reddit-data-project-assembly-0.1.jar processing-reddit-data-project.jar

ENTRYPOINT ["java", "-Dlogback.configurationFile=/logback.xml","-cp","/processing-reddit-data-project.jar", "keyword_service.KeywordMain"]