FROM java:8

VOLUME /tmp

ADD gam-site/build/libs/codeamatic-gam-0.1.0.SNAPSHOT.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]