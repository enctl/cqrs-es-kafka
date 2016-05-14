# Building Scalable Application
## ACME-Solution Reference Implementation

The purpose of this repository is to provide a reference implementation for a sample application called ACME-Solution
which is used as a sample application that should be built in a scalable way. The detailed description for the application architecture
and the styles underpinning it, please check my [blog](https://architectworks.ghost.io/) on Ghost.io

## Architectural Styles
The implementation adapts the following architectural styles:

1. Domain Driven Design
2. Command Query Responsibility Segregation
3. Event Sourcing

## Technology
The application uses the following set of light-weight technologies and frameworks:

1. RabbitMQ for Messaging
2. MongoDB for Persistence
3. Spring MVC for REST Services
4. Spring AMQP for Queueing and Messaging
4. Spring Data for Data Access Objects
5. Spring Boot for Bootstrapping and Boilerplates

## Prerequisites
Before running the above application, you'll need to install the following on your machine:

1. Maven 3.0.3 or later
2. MongoDB, I used v3.2 at the time of this implementation
3. RabbitMQ, I used v3.6 at the time of this implementation.

If you have a Mac OS, you can install the above components using [Homebrew](http://brew.sh/). The following commands install the above:
```
$ brew install maven
$ brew install rabbitmq
$ brew install mongodb
```

## Run
Once you have the above software installed, you can run the application in the following order:

1. Go to the root directory of the application and type: `mvn clean install`
2. Start the `application-api` project which is the entry point for REST URL's of the commands.
You can start it by navigating to its subdirectory and type: `mvn spring-boot:run`
3. Start the `application` project which contains the domain logic and event sourcing.
You can start it by navigating to its subdirectory and type: `mvn spring-boot:run`
4. Start the `query-api` project which represents the query part subscribing to the published events.
You can start it by navigating to its subdirectory and type: `mvn spring:boot:run`

## Variations
The above steps assume the default settings for Mongo and Rabbit. If you could like to change any of those settings, please
navigate to `application.properties` file that you'll find under each of the three applications above and then change the proper
values and re-run it again.

## Interaction
The command part can be tested by POSTing a JSON message to the following REST endpoint:
`http://localhost:8080/profiles/registrations` and the sample JSON request is:
```
{
    "username": "username",
    "email": "mail@domain.com",
    "password": "password"
}
```

As for the query part, you can test it by GET the following REST endpoint:
 `http://localhost:9090/profiles`

You can also check the messages as it passes through Rabbit using the Rabbit Admin console



