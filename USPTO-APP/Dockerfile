FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN apk update
RUN apk add mysql-client
RUN apk add apr-util-dbd_mysql
COPY USPTO-APP/wait-for-mysql.sh uspto/wait-for-mysql.sh
COPY USPTO-APP/target/*.jar uspto/uspto-app.jar