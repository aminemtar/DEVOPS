FROM openjdk:11
EXPOSE 8080
COPY target/DevOps-Project.jar DevOps-Project.jar
ENTRYPOINT ["java","-jar","/DevOps-Project.jar"]