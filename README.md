# Postgres Full Text Search Demo

## Build

To run build without tests use
``mvn clean verify -DskipTests``.

To run build with tests use: 
``mvn clean verify``.
To run tests you need a postgres database running on ``localhost:5432``.
You can up it database in docker container by command 
``docker run -d -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:14.8-alpine``.

## Docker

Create docker image with command 
``docker build -t postgres-fts-demo:1.0.0-SNAPSHOT .``.

## Run project

To run project use command 
``docker-compose up -d``.
Docker starts application and postgres database 

## Use FTS endpoint

You can use full text search endpoint by Postman on 
``http://localhost:8080/books/search?query=<a words to search>``.

A few examples of valid requests:

* ``query=Tolkien`` - will search for entries which includes this words.
* ``query="Catcher in the Rye"`` - will search for entries which includes such word combination.
* ``query=-Orwel -Tolkien`` - will return all entries except ones which includes defined words.
* ``query=-"George Orwell"`` - will return all entries except ones which includes defined word combination.

Curl examples:

* ``curl --location 'http://localhost:8080/books/search?query=Orwell'``
* ``curl --location 'http://localhost:8080/books/search?query=%22Catcher%20in%20the%20Rye%22'``
* ``curl --location 'http://localhost:8080/books/search?query=-Orwel%20-Tolkien'``
* ``curl --location 'http://localhost:8080/books/search?query=-%22George%20Orwell%22'``
