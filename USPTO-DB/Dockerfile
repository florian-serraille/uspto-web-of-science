# Derived from official mysql image (our base image)
FROM mysql:latest
# Add a database
ENV MYSQL_RANDOM_ROOT_PASSWORD true
ENV MYSQL_DATABASE USPTO
ENV MYSQL_USER USPTO-APP
ENV MYSQL_PASSWORD UCB
# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
ADD ./uspto-db-schema.sql /docker-entrypoint-initdb.d/
EXPOSE 3306
