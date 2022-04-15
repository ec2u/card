FROM httpd:2.4
COPY ./dist/static/ /usr/local/apache2/htdocs/