# email-rest-service

This is a Email Rest Service api built using Spring WebFlux. 
This is a reactive Java webservice api and exposes a Rest api for sending email.

## Run locally

## Run locally using profile
Use the following to run local profile which will pick up properties defined in the `application-local.yml` :

```
gradle bootRun --args="--spring.profiles.active=local"
```

```
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

Or you can do something like following too:

`mvn spring-boot:run -Dspring-boot.run.arguments="--EMAIL_HOST=<HOST> \
 --EMAIL_PORT=<PORT> \
 --EMAIL_USERNAME=<USERNAME> \
 --EMAIL_PASSWORD=<PASSWORD>"`
 
 
## Build Docker image

Build docker image using included Dockerfile.


`docker build -t imageregistry/email-rest-service:1.0 .` 

## Push Docker image to repository

`docker push imageregistry/email-rest-service:1.0`

## Deploy Docker image locally

`docker run -e EMAIL_HOST=<HOST> -e EMAIL_PORT=<PORT> \
 -e EMAIL_USERNAME=<EMAIL> -e EMAIL_PASSWORD=<PASSWORD> \
 --publish 8080:8080 imageregistry/email-rest-service:1.0`

Test email api locally using `curl`:

````
 curl -X POST http://localhost:8080/email -H 'Content-Type: application/json' \
 -d '{"from": "from@my.email", "to": "to@my.email", \
  "subject":"hello", "body": "welcome to planet Earth"}'
 ```` 
Test email on host using `curl`:
```
curl https://email-rest-service.sonam.cloud/email -H "Authorization: Bearer $JWT" \
  -H "Content-Type: application/json" -X POST \
 -d '{"from": "from@my.email", "to": "to@my.email", "subject":"hello", "body": "welcome to planet Earth"}'
  ```
## Installation on Kubernetes
Use a Helm chart such as my one here @ [sonam-helm-chart](https://github.com/sonamsamdupkhangsar/sonam-helm-chart):

```helm install emailapi sonam/mychart -f values.yaml --version 0.1.11 --namespace=backend```

### Pact verification
This will publish a Pact contract for the jwt-rest-service validate method to pactbroker
`mvn pact:publish`
`mvn clean install pact:publish`

Consumer pact will be published during docker build instruction.
