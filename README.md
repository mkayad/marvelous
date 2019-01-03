# marvelous
This is a repository containing few endpoints to query Marvel comic characters API (see http://developer.marvel.com/).

It also includes a feature to translate character powers using Google Translation API
# Requirements
The application is based on Maven, Java 8  and Spring boot 2.
It depends on few libraries such as Retrofit,Ehcache, RxJava, Jsoup,Swagger and commons-codec. Check the pom file for more details.


# How to run the application
To run successfully the application, it requires Marvel public and private API keys.
For the translation feature, it requires Google Translation API key.

There are two ways to run the application

## Running from Jar file
java -jar [jarname] -Dmv-pub-key=[XXX] -Dmv-priv-key=[YYY],-Dgg-api-key=[ZZZ]

where [XXX]=marvel public key, [YYY]=Marvel private key and [ZZZ]=Google Translate API key

## Running from maven 
If you have the source code, you can run it from the project folder assuming that maven is installed on the system.

mvn spring-boot:run -Dspring-boot.run.arguments=--mv-pub-key=[XXX],--mv-priv-key=[YYY],--gg-api-key=[ZZZ]
where [XXX]=marvel public key, [YYY]=Marvel private key and [ZZZ]=Google Translate API key

## Swagger documentation
The documentation of the REST endpoints can be accessed at http://localhost:8080/swagger-ui.html assuming the application is running on port 8080.


