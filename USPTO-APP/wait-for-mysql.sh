#!/bin/sh


HOST=db
DATABASE=USPTO
USER=USPTO-APP
PWD=UCB

until mysql -h${HOST} -u${USER} -p${PWD} ${DATABASE} -e '\q' 2> /dev/null; do
  echo "MySQL is unavailable - sleeping"
  sleep 3
done

echo "MySQL is up - executing command"

java -jar /uspto/uspto-app.jar
