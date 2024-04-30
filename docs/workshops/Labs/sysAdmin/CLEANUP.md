k delete -f cloud/k8/apps/http-amqp-source/http-amqp-source.yml
k delete -f cloud/k8/data-services/rabbitmq/MessageTopology/users/admin-user.yml
k delete -f cloud/k8/data-services/rabbitmq/MessageTopology/users/admin-user-permission.yml
k delete -f cloud/k8/data-services/rabbitmq/MessageTopology/Exchange-Queues/pos-topology.yaml
kubectl delete -f cloud/k8/data-services/rabbitmq/vmware-rabbitmq-1-node.yml