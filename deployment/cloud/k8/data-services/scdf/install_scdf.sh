#brew install kustomize

kubectl create namespace scdf-demo
kubectl config set-context --current --namespace=scdf-demo

#install Postgres Operator
curl https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/spring-modern-data-architecture/aef55c2adc62efcbd9d1478f0f9102722c3182f3/deployments/cloud/k8/dataServices/postgres/tanzu-postgres-operator-setup.sh | bash



kubectl config set-context --current --namespace=scdf-demo
kubectl create secret docker-registry regsecret \
--docker-server=https://registry.tanzu.vmware.com --docker-username=$PIVOTAL_MAVEN_USERNAME \
--docker-password=$PIVOTAL_MAVEN_PASSWORD

kubectl apply -f deployment/cloud/k8/data-services/postgres/postgres.yml

helm install scdf oci://registry-1.docker.io/bitnamicharts/spring-cloud-dataflow










export SCDF_K8_HOME=/Users/devtools/integration/scdf/scdf-kubernetes/commericial/spring-cloud-data-flow

cd $SCDF_K8_HOME
./bin/install-dev.sh --database postgresql --broker rabbitmq -o /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/deployment/cloud/k8/data-services/scdf/secret