### Strigo AMI



-----------
Working img

chmod 400 /Users/devtools/paaS/AWS/secrets/west-2/AWS-west-2.pem
ssh -i /Users/devtools/paaS/AWS/secrets/west-2/AWS-west-2.pem ubuntu@ec2-54-189-4-158.us-west-2.compute.amazonaws.com
ssh -i "AWS-west-2.pem" ubuntu@ec2-54-189-4-158.us-west-2.compute.amazonaws.com

AMI image

OLD: ami-00e1065e593d8b1cc

NEW ami-0998c4d3be4aeb4e7

UPDATED: ami-0f025242b24ee2d0f


UPDATEd: ami-07fccb989d5483120


----------------

## Setup

sudo apt-get install openjdk-11-jdk
sudo apt install docker.io


curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.11.1/kind-linux-amd64
chmod +x ./kind
sudo mv ./kind /usr/bin/kind




vi k8-1wnode.yaml


```yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
- role: worker

```

sudo systemctl start docker
sudo systemctl enable docker

curl -LO https://dl.k8s.io/release/v1.21.0/bin/linux/amd64/kubectl
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl



**kubectl**

    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
    sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
    alias k='kubectl'

    rm -rf /$HOME/.kube
    sudo cp -r /root/.kube /$HOME/.kube
    sudo chown -R $USER $HOME/.kube


**Instal Helm**

    curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3
    chmod 700 get_helm.sh
    ./get_helm.sh

**Cert Manager Install**

kubectl create namespace cert-manager
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true


**GemFire**
kubectl config current-context
kubectl create namespace gemfire-system

export HARBOR_USER=
export HARBOR_PASSWORD=


kubectl create secret docker-registry image-pull-secret --namespace=gemfire-system --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD


kubectl create rolebinding psp-gemfire --clusterrole=psp:vmware-system-privileged --serviceaccount=gemfire-system:default


wget https://cloud-native-data.s3.amazonaws.com/gemfire-operator-1.0.1.tgz
helm install gemfire-operator gemfire-operator-1.0.1.tgz --namespace gemfire-system


vi gemfire1.yaml
```
apiVersion: gemfire.tanzu.vmware.com/v1
kind: GemFireCluster
metadata:
 name: gemfire1
spec:     
  image: registry.pivotal.io/tanzu-gemfire-for-kubernetes/gemfire-k8s:1.0.0
  locators:           
    replicas: 1                   
  servers:
    replicas: 1     
```

k apply -f ./gemfire1.yaml

apt install git

----
curl -O https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz
tar zxvf openjdk-11.0.1_linux-x64_bin.tar.gz
sudo mv jdk-11.0.1 /usr/local/

sudo vi /etc/profile.d/jdk11.sh

# create new
export JAVA_HOME=/usr/local/jdk-11.0.1
export PATH=$PATH:$JAVA_HOME/bin

source /etc/profile.d/jdk11.sh


sudo apt install maven


git clone https://github.com/ggreen/spring-geode-showcase.git



sudo groupadd docker
sudo usermod -aG docker ${USER}
newgrp docker


**Instal k9s**
wget https://github.com/derailed/k9s/releases/download/v0.24.14/k9s_Linux_x86_64.tar.gz
tar xvf k9s_Linux_x86_64.tar.gz
sudo cp ./k9s /usr/bin/


*Shell into locator*
gfsh
connect

