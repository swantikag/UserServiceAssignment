FROM openjdk:8-jre-alpine
COPY ./target/scala-2.12UserManagementSystem-assembly-0.1.jar /
CMD java -jar ./target/scala-2.12/UserManagementSystem-assembly-0.1.jar
