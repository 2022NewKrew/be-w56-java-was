# for macbook m1
FROM --platform=linux/x86_64 mysql
ADD ./mysql-init-files /docker-entrypoint-initdb.d

EXPOSE 3306 33060

ENV MYSQL_ROOT_PASSWORD test
ENV MYSQL_DATABASE JAVA_WAS

CMD ["mysqld"]