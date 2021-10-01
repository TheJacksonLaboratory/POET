# POET
A Full-Stack annotation application to generate data for disease and associated ontologies.

# Setup

Java 1.8

Intellij

- Import project as gradle project
- Ensure the gradle runtime is the wrapper (usually defaults)

Requirements
- Node
- Npm

# Running

#### Development (2 tabs)

To Initialize DB

` 
    ./gradlew bootRun --args="--initializePoet=true" 
`

Run Default

`
    ./gradle bootRun
`

In src/main/poet

`
    npm run start
`

Navigate to http://localhost:4200/, endpoints will be served at http://localhost:8080/api/v1/


# Unit & Functional Testing Before Deployment

`
    ./gradlew test_release
`

# Building For Deployment
Test Environment Internal Only

`
    ./gradlew bootWar
`

Production Environment Deployment
`
    ./gradlew bootWar --args="--release=true"
`


# Contributors

Michael [iimpulse](https://github.com/iimpulse) Gargano
