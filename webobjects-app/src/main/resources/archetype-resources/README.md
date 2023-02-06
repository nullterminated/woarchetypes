This is a project to 
* run a WebObjects application 
* inside a docker container
* pushing it to an AWS ECR repository

Prerequisites,

* Java 17 installed
* Maven installed
* Docker installed and configured so that you do not need to sudo to use docker
* amazon-ecr-credential-helper installed and configured so that you can push to your ecr repository without needing to use aws get-login-password

To build the project,

```
mvn clean verify -Pdocker
```

To run the project,

```
docker run --rm -it --hostname=localhost -p 8080:8080 your.app/wodocker:latest
```

Substitute localhost for your actual domain name, as whatever hostname you specify here is used to generate complete urls.

To install the project in AWS ECR, set the appropriate aws properties 

```
# command line
mvn clean install -Pdocker,aws-ecr -Daws.acctid=123456789012 -Daws.region=us-east-1
# or with profile properties
mvn clean install -Pdocker,aws-ecr
```

