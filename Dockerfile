FROM openjdk:8-jre-alpine
<<<<<<< HEAD
<<<<<<< HEAD
COPY UserManagementSystem-assembly-0.1.jar /
CMD java -jar ./UserManagementSystem-assembly-0.1.jar
=======
COPY ./target/scala-2.12/UserManagementSystem-assembly-0.1.jar ./
=======
COPY target/scala-2.12/UserManagementSystem-assembly-0.1.jar .
>>>>>>> 06f63c79dcba4cf66676b3dcf5f5a1c862221024
CMD java -jar UserManagementSystem-assembly-0.1.jar
>>>>>>> c5f06c484e689a538d0291edfd485e0c15d5db5d
