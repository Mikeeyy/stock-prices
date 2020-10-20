FROM openjdk:11-jdk
EXPOSE 8110
ADD build/libs/stockprices-0.0.1-SNAPSHOT.jar stockprices.jar
ENTRYPOINT ["java","-jar","/stockprices.jar"]