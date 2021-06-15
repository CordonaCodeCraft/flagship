FROM centos
RUN yum install -y java-11-openjdk-devel
VOLUME /tmp
ADD target/flagship-1.0-SNAPSHOT.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/ ./urandom", "-jar", "/myapp.jar"]
EXPOSE 8080