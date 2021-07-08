# POET
A Full-Stack annotation application to generate data for disease and associated ontologies.

# Setup

Java 1.8

Intellij

- Import project as gradle project
- Ensure the gradle runtime is the wrapper (usually defaults)


# Running

####Development (2 tabs)

Requirements
- Node
- Npm

In root directory provide args where necessary to initialize database: 
` 
./gradlew bootRun --args="--initializePoet=true" 
`

In src/main/poet
`
npm run start
`

Navigate to http://localhost:4200/, endpoints will be served at http://localhost:8080/api/v1/

#### Testing

To test the app under a singular url

`
./gradlew testApp
`

Navigate to http://localhost:8080/


# Building for deployment

`
./gradlew war -DclientTest=true
`

# Contributors

Michael [iimpulse](https://github.com/iimpulse) Gargano
