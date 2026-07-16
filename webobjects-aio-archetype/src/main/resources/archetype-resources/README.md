# Prerequisites

- Java 25
- Maven

# Build instructions

1. Install WebObjects from Apple into your ~/.m2 repository.
    1. mvn io.github.wocommunity:woinstall-maven-plugin:woinstall
2. Build & install the womodular project.
    1. git clone https://github.com/wocommunity/womodular.git
    2. cd womodular
    3. mvn clean install
3. Check out and build this project in eclipse or on the command line.
    1. In eclipse, you should be able to right click on ${artifactId}.launch and Run as/Debug as the application.

	
