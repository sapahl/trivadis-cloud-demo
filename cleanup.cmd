@REM Cleanup producer
oc delete all --selector app=producer
oc delete bc producer

@REM Cleanup frontend
oc delete all --selector app=frontend
oc delete bc frontend

@REM Cleanup processor
oc delete all --selector app=processor
oc delete bc processor

@REM Cleanup processor
oc delete -f kubernetes\pipeline.yml

@REM Cleanup kafka
oc delete -f kubernetes\kafka-topic.yml
oc delete -f kubernetes\kafka-cluster.yml

