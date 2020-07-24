@REM Create Kafka
@REM oc create -f kubernetes\kafka-cluster.yml
oc create -f kubernetes\kafka-topic.yml

@REM Create Build Configuration Frontend
oc new-build --strategy docker --dockerfile - --code cloud-demo-frontend --name frontend < cloud-demo-frontend\src\main\docker\Dockerfile.jvm
oc start-build --from-dir cloud-demo-frontend frontend

@REM Create Build Configuration Processor
oc new-build --strategy docker --dockerfile - --code cloud-demo-processor --name processor < cloud-demo-processor\src\main\docker\Dockerfile.jvm
oc start-build --from-dir cloud-demo-processor processor

@REM Create Build Configuration Producer
oc new-build --strategy docker --dockerfile - --code cloud-demo-producer --name producer < cloud-demo-producer\src\main\docker\Dockerfile.jvm
oc start-build --from-dir cloud-demo-producer producer

@REM Create Applications
oc new-app --image-stream trivadis-cloud-demo/processor --name processor
oc new-app --image-stream trivadis-cloud-demo/frontend --name frontend
oc new-app --image-stream trivadis-cloud-demo/producer --name producer

@REM Expose Routes
oc expose svc/frontend
oc expose svc/producer

@REM Create Pipeline
oc create -f kubernetes\pipeline.yml