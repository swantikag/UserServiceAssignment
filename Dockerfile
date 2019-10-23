FROM openjdk:8-jre-alpine
<<<<<<< HEAD
COPY UserManagementSystem-assembly-0.1.jar /
CMD java -jar ./UserManagementSystem-assembly-0.1.jar
=======
COPY ./target/scala-2.12/UserManagementSystem-assembly-0.1.jar ./
CMD java -jar UserManagementSystem-assembly-0.1.jar
>>>>>>> c5f06c484e689a538d0291edfd485e0c15d5db5d
