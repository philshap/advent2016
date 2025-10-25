Advent 2016 in Java

To run from the command line:

- get the AoC session token by loading any AoC input page in a web browser
  and copy the value of the `session` Cookie.

- compile:
```shell
cd src
javac -d out src/advent2016/*.java
```

- run
```shell
SESSION_TOKEN="<AoC session token>" java -cp out advent2016.Main
```

- run tests
 ```shell
java -cp out advent2016.Main test
```
