# TableTop Customers microservice
[![Build Status](https://travis-ci.org/TableTopLtd/tt-customers.svg?branch=master)](https://travis-ci.org/TableTopLtd/tt-customers)
## Prerequisites

```bash
docker run -d --name tt-customers-db -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=customer -p 5435:5432 postgres:latest
```

## When developing

Run
```bash
mvn clean package
```
from repo root directory.

To run application local jar, run:
```bash
java -jar api/target/*.jar
```

Microservice finds database through ip

To test the service you should go to
```
http://localhost:8083/v1/customers
```
To see a list of all customers.

```
http://localhost:8083/v1/customers/1
```
To see the first one, etc.

## Build docker image
```bash
docker build . -t tt-customers:X
```

[Optional] Define your own X

## Run application in Docker
```bash
docker run -p 8081:8081 tt-customers:X
```