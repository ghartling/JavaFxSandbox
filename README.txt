# Install the Oracle jar
# You can install the Oracle Jar into your local Maven repsository using this command: 
mvn install:install-file -Dfile=lib\ojdbc7.12.1.0.2.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar

# To connect to the Spring Maven Repo you will need to download the certificate (BASE 64 encoded) from:
https://repo.spring.io/milestone/

# Install using (default password is changeit):
keytool -import -storepass changeit -file repo-spring-io.cer  -keystore %JAVA_HOME%/jre/lib/security/cacerts

# To Remove a key:
keytool -delete -keystore %JAVA_HOME%/jre/lib/security/cacerts -alias mykey

# Instructions:
https://stackoverflow.com/questions/25911623/problems-using-maven-and-ssl-behind-proxy

# Build with Maven (from root folder, where pom.xml resides):
mvn package
mvn clean verify package

# Run with Maven
mvn spring-boot:run

# If you need to set the location of the keystore:
mvn package -Djavax.net.ssl.trustStore=%JAVA_HOME%/jre/lib/security/cacerts

# Test Services Using curl:
curl -i -H "Content-Type: application/json"  http://localhost:8080/business-settings

curl -i -H "Content-Type: application/json"  http://localhost:8080/options

# Test with REMOTE_USER header
curl -i -H "Content-Type: application/json" -H "REMOTE_USER: 1074040"  http://localhost:8080/dsinformationview/1074040

# Some useful Maven commands:
mvn dependency:copy-dependencies -DoutputDirectory=lib-maven

mvn dependency:sources

# To access the UI:
http://localhost:8080/home

# H2 console
http://localhost:8080/h2-console

#
# Testing/ debugging
#

# To the run the tests
mvn site -DgenerateReports=false 
mvn surefire-report:report


# If you get this error while running maven:

�Resolution will not be reattempted until the update interval of central has elapsed or updates are forced�

Run this command:

mvn -U clean package

To build without running any tests:

mvn clean install -Dmaven.test.skip=true


To create eclipse project file while building (need to do this initially and when pom.xml changes), without running any tests:

mvn clean install eclipse:eclipse -Dmaven.test.skip=true

To run a test class:

mvn -Dtest=ProposalCreateQueryTest test

To run a test method:

mvn -Dtest=TestCircle#mytest test


If you get a "not enough memory" error while building:

set MAVEN_OPTS=-Xms512m -Xmx2048M

#
# URL to the application
#
If you ran it as a Spring Boot App:
http://localhost:8080/home

If you deployed it on an external Tomcat:
http://localhost:8080/businessSettings/home

# weather app
http://localhost:8080/weather/stn/favorite?REMOTE_USER=1074040
http://localhost:8080/weather/stn/1074040?REMOTE_USER=1074040

