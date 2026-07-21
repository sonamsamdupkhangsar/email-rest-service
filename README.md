# email-rest-service

 
This is a Email Rest Service api built using Spring WebFlux. 
This is a reactive Java webservice api and exposes a Rest api for sending email.

This uses [token-filter](https://github.com/sonamsamdupkhangsar/token-filter) gradle project for creating JwtDecoder with custom IssuerUrl.

## How is this service used?
```mermaid
flowchart TD
 UserService[<a href='https://github.com/sonamsamdupkhangsar/user-rest-service'>user-rest-service</a>] --> create[/Create account/] -->Account[account-rest-service]
 Account-->accountCreated[/Account Created/]--"send email"--> email[email-rest-serivce]
 User[user request] --> emailActivationLink[/Email activation link/]--> email 
 User --> emailAuthenticationId[/Email authenticationId/]--> email
 User -->emailSecret[/Email secret for password reset/] -->email
```

## Run locally

## Run locally using profile
Use the following to run the Eureka profile, which picks up properties defined in `application-eureka.yaml`:

```bash
source ~/.zshrc
SPRING_PROFILES_ACTIVE=eureka ./gradlew bootRun
```

Local SMTP configuration is read from the existing shell environment:

```text
EMAIL_HOST=box.openissuer.com
EMAIL_PORT=587
EMAIL_USERNAME=<OpenIssuer mailbox address>
EMAIL_PASSWORD=<OpenIssuer mailbox password>
```

Keep the values in the local shell configuration and do not commit them. `account-rest-service` separately reads
`EMAIL_FROM_USER`; set it to the same OpenIssuer mailbox address so the message From address matches the authenticated
SMTP account.
 
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
