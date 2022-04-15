docker build --tag ec2u/card .

docker run -dit --name card -p 8080:80 ec2u/card