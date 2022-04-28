docker build --file Dockerfile --tag ec2u/card:latest code

docker run -it --rm --name ec2u-card -p 8080:80 ec2u/card:latest


