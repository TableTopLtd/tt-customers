# TableTop Places microservice
[![Build Status](https://travis-ci.org/TableTopLtd/tt-places.svg?branch=master)](https://travis-ci.org/TableTopLtd/tt-places)
## Prerequisites

```bash
docker run -d --name tt-places-db -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=place -p 5434:5432 postgres:latest
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
http://localhost:8082/v1/places
```
To see a list of all places.

```
http://localhost:8082/v1/places/1
```
To see the first one, etc.

## Build docker image
```bash
docker build . -t tt-places:X
```

[Optional] Define your own X

## Run application in Docker
```bash
docker run -p 8081:8081 tt-places:X
```


## Running and configuring etcd server

To run etcd server in docker run
```bash
    $ docker run -d -p 2379:2379 \
        --name etcd \
        --volume=/tmp/etcd-data:/etcd-data \
        quay.io/coreos/etcd:latest \
        /usr/local/bin/etcd \
        --name my-etcd-1 \
        --data-dir /etcd-data \
        --listen-client-urls http://0.0.0.0:2379 \
        --advertise-client-urls http://0.0.0.0:2379 \
        --listen-peer-urls http://0.0.0.0:2380 \
        --initial-advertise-peer-urls http://0.0.0.0:2380 \
        --initial-cluster my-etcd-1=http://0.0.0.0:2380 \
        --initial-cluster-token my-etcd-token \
        --initial-cluster-state new \
        --auto-compaction-retention 1 \
        -cors="*"
   ```
   
To run etcd browser from https://github.com/henszey/etcd-browser I think it is enough to run
```bash
docker run --rm --name etcd-browser -p 0.0.0.0:8000:8000 --env ETCD_HOST=10.10.0.1 --env AUTH_PASS=doe -t -i etcd-browser   
```
This should start a server at localhost:8000. If it doesn't see linked repo for full instructions.
   