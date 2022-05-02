FROM httpd:2.4-alpine
COPY code/static/ /usr/local/apache2/htdocs/