FROM openjdk:22-jdk
ADD target/blog-platform.jar blog-platform.jar
ENTRYPOINT ["java", "-jar", "/blog-platform.jar"]