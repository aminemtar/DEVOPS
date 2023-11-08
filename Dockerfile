FROM openjdk:11
EXPOSE 8082
COPY target/DevOps-Project.jar DevOps-Project.jar
ENTRYPOINT ["java","-jar","/DevOps-Project.jar"]