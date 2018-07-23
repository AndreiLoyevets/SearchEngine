# Simple Search Engine Demo Project

Maven project consisting of 2 modules - client and server.

### Server
Works with small documents where each document contains a series of tokens (words) separated by spaces. Document are
represented as String.

The usage model:
- Put documents into the search engine by key
- Get document by key
- Search on a string of tokens (words) to return keys of all documents that contain all tokens in the set

Documents are stored in server's memory.
To keep things simple we assume that there will be no overwrites of a key with a new document.
Simplest static configuration is used (no service discovery, replicas, balancing etc.).

### Client

Client has a hard-coded behaviour making requests to server.

## Configuration

Server request mappings are hardcoded, listening to requests under `/searchengine`  path.
Server port (8080 by default) is configured under `server.port` property in `server/src/main/resources/application.properties`.
Client sends requests to server url configured under `server.url` property in `client/src/main/resources/application.properties`.
For example, `server.url=http://localhost:8080/searchengine`.

## Installation

Java 8 and Maven should be installed on the machine to build and run the project.
Download and unzip the project. From the root project directory run:

`mvn package`

This  will generate two executable jars - `server/target/server-1.0-SNAPSHOT.jar` for server and `client/target/client-1.0-SNAPSHOT.jar` for client.

## How To Run
Start the server with command:

`java -jar server/target/server-1.0-SNAPSHOT.jar`

After server is started and "Started ServerRunner..." message is displayed, start the client:

`java -jar client/target/client-1.0-SNAPSHOT.jar`

Client starts, sends requests to server, logs them to console and finishes.

### Server requests

PUT and GET document requests are sent to `http://<address>:<port>/searchengine/<key>` endpoint.
Search phrase can be passed as request parameter: `http://<address>:<port>/searchengine?searchPhrase=<search phrase>`

### ToDo
- Docker
- probably use maven-assembly-plugin in order not to package properties inside jar but put near jar for convenient config after build