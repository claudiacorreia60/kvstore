# Key-Value Store
Basic implementation of a persistent Key-Value Store that provides a REST API.

## Create JAR file

The JAR file for the KV Store was already created and can be found in `artifacts`.
The commands used to create the JAR were the following.

```
$ javac src/*.java -d artifacts
$ cd artifacts
$ jar cfe kvstore.jar KVStoreServer *.class
$ rm *.class
```

## Run server

To run the KV Store HTTP server use the following commands and specify the `ip_address` and `port` to which the server will be bound to.

```
$ java -jar artifacts/kvstore.jar <ip_address> <port>
```

Example:


```
$ java -jar artifacts/kvstore.jar localhost 3000
```

## REST API

### Read

To read the `value` for a specific `key`, a `GET` `/<key>` request can be sent to the server.

In case of **success**, the server will respond with an HTTP `200 OK` with the `value` in the content.
If the **key doesn't exist**, the server will respond with an HTTP `404 NOT FOUND`.

### Create or Update

To create or update a KV entry, a `PUT` `/<key>` request can be sent to the server, with the `value` in the body content.
The server will respond with an HTTP `204 NO CONTENT`.

### Delete

To delete a KV entry, a `DELETE` `/<key>` request can be sent to the server.
The server will respond with an HTTP `204 NO CONTENT`.

### Example requests

After starting the server, we can use cURL to send HTTP requests to the server.
These examples assume the HTTP server is running on `localhost`, port `3000`.

1. Write a value to the key `foo`:

`$ curl -i -X PUT 'http://localhost:3000/foo' -H 'Content-Type: application/octet-stream' --data-binary 'hello world!'`

`HTTP/1.1 204 No Content`

2. Fetch the value of foo:

`$ curl -i 'http://localhost:3000/foo'`

```
HTTP/1.1 200 OK

Content-Type: application/octet-stream

Content-Length: 12

hello world!
```

3. Fetch the value of bar (which does not exist):

`$ curl -i 'http://localhost:3000/bar'`

`HTTP/1.1 404 Not Found`

4. Delete the key foo:

`$ curl -i -X DELETE 'http://localhost:3000/foo'`

`HTTP/1.1 204 No Content`
