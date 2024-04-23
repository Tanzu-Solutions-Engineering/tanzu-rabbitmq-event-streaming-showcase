kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-hub.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-hub --for=condition=Ready --timeout=160s

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-hub-user.yml

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/hub-topology.yaml

#------------
# Site 1

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-1.yml

kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site1 --for=condition=Ready --timeout=160s


kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-1-user.yml

kubectl apply  -f deployment/cloud/k8/data-services/rabbitmq/multi-site/site1-topology.yaml

#------------
# Site 2

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-2.yml

kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site2 --for=condition=Ready --timeout=160s


kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-2-user.yml

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site2-topology.yaml

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/site2-topology.yaml

#------------
# Site 3

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-3.yml

kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site3 --for=condition=Ready --timeout=160s

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/rabbitmq-site-3-user.yml

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/site3-topology.yaml

# ------------------
# Shovels

kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/multi-site/site1-hub-replication.yml