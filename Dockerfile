FROM php:7.2-apache

MAINTAINER EC2U Connect Centre <cc@ml.ec2u.eu>

COPY code/static /var/www/html/
COPY code/php /var/www/html/
COPY code/php/v1.php /var/www/html/v1
COPY httpd/sites /etc/apache2/sites-available
COPY httpd/certs /etc/apache2/certs
COPY httpd/apache2-foreground /usr/local/bin
COPY shibboleth /tmp/shibboleth
COPY tenants.json /etc/ec2u-card/tenants.json

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get -q -y install apt-utils

RUN apt-get update \
    && apt-get -q -y full-upgrade \
    && apt-get -q -y install vim\
    && apt-get -q -y install wget \
    && apt-get -q -y install libapache2-mod-shib2 \
    && a2enmod auth_basic \
    && a2enmod rewrite \
    && a2enmod headers \
    && a2enmod ssl \
    && a2enmod shib \
    && a2dissite default-ssl.conf \
    && a2dissite 000-default.conf \
    && a2ensite card.ec2u.eu.conf

COPY shibboleth /etc/shibboleth/

EXPOSE 80