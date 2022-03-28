FROM openjdk:jdk-oracle

WORKDIR /api-speak
ADD target/api-speak.jar app.jar

CMD java $JAVA_OPTS -jar app.jar
