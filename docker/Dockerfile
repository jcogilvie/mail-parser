FROM amazoncorretto:11
EXPOSE 0.0.0.0:8080:8080
RUN mkdir service/
COPY maven service/
CMD ["java", "-jar", "service/${project.build.finalName}.jar"]