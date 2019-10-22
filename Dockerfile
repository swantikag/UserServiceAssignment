FROM openjdk:8-jre-alpine
COPY UserManagementSystem-assembly-0.1.jar /
CMD java -jar ./UserManagementSystem-assembly-0.1.jar
