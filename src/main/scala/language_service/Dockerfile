FROM openjdk:8-jre-alpine

COPY /target/scala-2.13/processing-reddit-data-project-assembly-0.1.jar processing-reddit-data-project.jar

ENTRYPOINT ["java","-cp","/processing-reddit-data-project.jar", "generator_service.GeneratorMain"]