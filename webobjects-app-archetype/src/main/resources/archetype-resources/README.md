This is a project to 
* run a WebObjects application 
* inside a docker container
* pushing it to an AWS ECR repository

Prerequisites,

* Java 17 installed
* Maven installed
* Docker installed and configured so that you do not need to sudo to use docker
* amazon-ecr-credential-helper installed and configured so that you can push to your ecr repository without needing to use aws get-login-password

Install WebObjects

1. Install WebObjects from Apple into your ~/.m2 repository.
    1. mvn io.github.wocommunity:woinstall-maven-plugin:woinstall
2. Build & install the womodular project.
    1. git clone https://github.com/wocommunity/womodular.git
    2. mvn clean install -f womodular/pom.xml
    3. rm -rf womodular

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

