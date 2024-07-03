# quarkus-smallrye-kafka-error-swalling

This project uses Quarkus, alongside with:

- SmallRye Kafka (messaging-kafka)
- container-image-docker
- rest-jackson
- apicurio-registry-avro

extensions.

The goal is to reproduce a bad behaviour detected when an Error ( not exception ) is thrown from the kafka polling
thread.

## Running the example

This command will run the build, including integration tests against a running docker container.

```shell script
mvn verify -Dquarkus.container-image.build=true
```

## Conclusion

I could not reproduce the error. Even producing records with snappy and using alpine, the error was not thrown.

## References

The original error, which only appeared once while remote debugging, was:

```text
java.lang.UnsatisfiedLinkError: /tmp/snappy-unknown-a8786dca-a71e-4bd0-9e9c-7af660e76e13-libsnappyjava.so: 
Error loading shared library ld-linux-x86-64.so.2: No such file or directory (needed by /tmp/snappy-unknown-a8786dca-a71e-4bd0-9e9c-7af660e76e13-libsnappyjava.so)
```
