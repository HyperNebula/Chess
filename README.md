# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[Main Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspXI3l5d5LsKYoSm6MpymW7xKoFvLBZZ1kRVuKrBhqTT6H8OxGBAahoAA5MwxxgCA8Tbh5ln+sy0yXtASAAF4oBwUYxnGhRaUmlSiWmTgAIwETmqh5vM0FFiW9Q+DVep1Y1ux0U27mCsO-JjhOKDPvE56Xtey2LpUD5rgG21botnUPLC-qOeKGSqABmA6fClVgYR+nzCRqHfBRVH1u9tHoY9XXIKmMC4fhowvTFxGkZ9l7fchv1ofRpieN4fj+F4KDoDEcSJBjWOOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMX2Ieg-3aeZ-oUTNeQFPUAA8LNIeU92c-CvrOl2NlCYTDky6ezlqK55VLbeK0wIyYAbVt8Gs2gc5BQ6IWiuKT7HfIsrykLbOpeqR16jAcM5WoMBoBAzBWmicULgKZ3ud2vb9qdT2ulN8QzU1LUoLGCns2d3UwOm-Xg4Nw0FmMY3QBNYcR3NjYMbtvuJS69tXidkuq4b9IcCg3DHpeOtl-IBvxUb+3CjXdeGBtr4V2dD31ATCv-gggFiyB1T+mQPj7nHyZA9hIN4Vm80MSjzH+Ci67+Ng4oavxaIwAA4kqGjEyHjRH9TdP2EqzOw3rceVAPxq1VADW82gAvW2gIsPSTkt6jIByCfHMGJQFqEViSFWnlW70g1kybWP8W4+3vKFU2pdIpWwfkhTAtsNQbUdnrZ2Ao3Ye2tDkb2+4J6dgDn2GBF9qrc3frNKOMd4wdXniUMAPVk7Zn5GnUaxYs4KhziwpqDYkaFxoVZEuPdy6dkrnA0cmsIGqAxCg6hxtNaVgQMfJUHppF+0AQJQ+EAABm+i5i90Uf3MW9Q1E3Tuv-YxElnq3xzAWBo4wPEoAAJLSBQr1cIwRAggk2PEXUKA3SchomRMYyRQBqhiZBBG3xfEADlUnQwuJ0TSk8AbwAXjwpeYMfGny8eUuYASgkhLCcsCJUSUlQw+iCRJIBklETetDEEmTskfVyavRiqMAgcAAOxuCcCgJwMQIzBDgFxAAbPANaVjDBFGKQAtxZNWgdBvnfMOcMsx9LmPktxHMLqlmYR-Nq38cHoHKMcpUWTTmi0uVs2RUtDxyBQBAjEcA1oQKgcrU6VI1bwM1kg+5+sqHBXbibCU8iLZRR-ng1UBDLxEKQiQ127sYCe0oUY4uUsez0ODhLbZkoxENUjtGaObU56A24bwgaAj8xCPGqI65s1JFNiJRSz59QkXABgWCquB5AVKgxCcgKRj4WPjWTKBhz97ErKPL8pUTjR5vK-DI3SyxfE1PqME0JMAzmJi4cDUGWYDVKiNTAE1gQzX52GRvSwtdbKbGxkgBIYB3V9ggF6gAUhAcUayYjtLVBs7hHzJJNGZDJHovj766yQlmbACBgDuqgHACAtkoBrENdIc1GEPyqu5Z-O5qaHk2p+Jm7Nub817AAOosD8TTHoAAhfiCg4AAGl0l2sCcaupzqXECusgAK1DWgP5IbrooEJErNyfcxXKJ8lrBuyDYVtyFAis2L5tCW1ftWwo+DS5YqxhwXKpC8UEqwPyjsgrXaB2VU+qq1LWF0vYe1ApmFikspTmykahZhGlkmtyiRQzH3+0wQoz5Sifa1FUUqTtlgAnSqHZouFe6wrrggVgmAASd2LmJQ4gxKV0UwCaBARmbUYBht8ZWM08pzGvvJe+0sgYWNhiQmwhlnCmXA3TJmUYyxU7stA5yk0LGYC1h+kM0FMhwWjmneKP5RbVL1soI26Aax-LYd3cuZk2AtDokVUet9L951oC1WPd5riaj1GnrPQTRTmWlJXi69eaMvBZu9b6vz8pEDBlgMAbAGbCCfxgNG8wsbSzk0ptTWmvRjBP3Ol+WoIwzIOYnSXTuTJ1p9g0aK5T4r6gFfRDyErcq90yFroV3RRh4ODjLZc+o0gGtgDszqiyF8XMjkZe5q1y9RhDKAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
