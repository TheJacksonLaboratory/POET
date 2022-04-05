# POET
A Full-Stack annotation application to generate data for disease and associated ontologies.

# Setup

Java 11

Intellij

- Import project as gradle project
- Ensure the gradle runtime is the wrapper (usually defaults)

Requirements for development
- Node
- Npm
- Java 11
- Mysql or Mariadb

# Running

### Setup the database

First, open the database console.

```shell
sudo mysql -u root
```

Then create the database `poet`, a user (e.g. `poetdev`), and grant `poetdev` the privileges for the `poet` database.
```
> create database poet;
> create user 'poetdev'@localhost identified by 'password1';
> grant all privileges on poet.* to 'poetdev'@localhost;
> flush privileges;
```
> Replace `password1` with a better choice.

Configure datasources under, replace `username` and `password` with the credentials from the previous step:
-   ``/src/main/resources/application-*.yml``

Initalize Data (~30min)

`
./gradlew bootRun --args="--initializePoet=true"
`
### Run the app

Open in two terminals

Run Server

`
./gradle bootRun
`

Run Client src/main/poet

`
cd src/main/poet;
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
    ./gradlew bootWar -Dspring.profiles.active=stage
`

Production Environment Deployment
`
    ./gradlew bootWar  -Dspring.profiles.active=production
`


# Contributors

Michael [iimpulse](https://github.com/iimpulse) Gargano
