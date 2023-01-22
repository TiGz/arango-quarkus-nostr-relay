# arango-quarkus-nostr-relay

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## prerequisites

Start a local database:

```shell script
docker run -e ARANGO_ROOT_PASSWORD=test -p 8529:8529 --rm arangodb:latest
``` 

## test

```shell script
mvn test
```

## test native

```shell script
mvn verify -Pnative
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `arango-quarkus-nostr-relay-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/arango-quarkus-nostr-relay-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/arango-quarkus-nostr-relay-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .


## Request Version

```shell script
curl http://localhost:8080/version
```


## Example NOSTR Event


    {"id": "1910092961fe1ae6b1ce8eecc1f711a025d0c23986e4257a188bf57d296f313a","pubkey":"3b2e2252f113791ec8ec973b8285021a7c49e80d5c9f54d2b28b610b303b8425","created_at":1674391523,"kind":1,"tags":[],"content":"test","sig":"eb3cf16a2441243cd35e73642ade2b1a95a8b16c3c24e1e8501b0f54438e87f0e77271d0981ebe977f962794f2cf2291b48c8e4c4dee62909a9980bda7b57b53"}

    ["EVENT",{"id": "1910092961fe1ae6b1ce8eecc1f711a025d0c23986e4257a188bf57d296f313a","pubkey":"3b2e2252f113791ec8ec973b8285021a7c49e80d5c9f54d2b28b610b303b8425","created_at":1674391523,"kind":1,"tags":[],"content":"test","sig":"eb3cf16a2441243cd35e73642ade2b1a95a8b16c3c24e1e8501b0f54438e87f0e77271d0981ebe977f962794f2cf2291b48c8e4c4dee62909a9980bda7b57b53"}]


    {
    "id": "1910092961fe1ae6b1ce8eecc1f711a025d0c23986e4257a188bf57d296f313a",
    "pubkey": "3b2e2252f113791ec8ec973b8285021a7c49e80d5c9f54d2b28b610b303b8425",
    "created_at": 1674391523,
    "kind": 1,
    "tags": [],
    "content": "test",
    "sig": "eb3cf16a2441243cd35e73642ade2b1a95a8b16c3c24e1e8501b0f54438e87f0e77271d0981ebe977f962794f2cf2291b48c8e4c4dee62909a9980bda7b57b53"
    }