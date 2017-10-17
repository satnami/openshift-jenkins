FROM fabric8/java-jboss-openjdk8-jdk:1.2.3 
ENV JAVA_APP_JAR swagger-spring-1.0.0.jar 
ENV AB_ENABLED off 
ENV JAVA_OPTIONS -Xmx512m 
EXPOSE 8080 
ADD target/swagger-spring-1.0.0.jar /deployments/

