# cloud-demo-frontend project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `cloud-demo-frontend-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/cloud-demo-frontend-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/cloud-demo-frontend-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

## Steps
- Install Minishift and create an OpenShift Cluster
- Create Kafka Operator
- Create Kafka Cluster
- Retrieve Key for TLS encryption
```
oc extract secret/kafka-cluster-cluster-ca-cert --keys=ca.crt --to=- > src/main/resources/ca.crt
keytool -import -trustcacerts -alias root -file src/main/resources/ca.crt -keystore src/main/resources/keystore.jks -storepass password -noprompt
```
- Create new build
```
oc new-build --strategy docker --dockerfile - --code . --name frontend < src\main\docker\Dockerfile.jvm
oc start-build --from-dir . frontend 
```
- Create new Application
```
oc new-app --image-stream trivadis-cloud-demo/frontend --name frontend
```

## Cleanup
```
oc delete all --selector app=frontend
```