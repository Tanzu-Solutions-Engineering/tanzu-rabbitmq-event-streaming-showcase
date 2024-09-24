#!/bin/bash

set echo off
#set -x #echo on

# Set GemFire Pre-Requisite

kubectl create namespace cert-manager

helm repo add jetstack https://charts.jetstack.io

helm repo update



# Read more about the installation in the

kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.15.3/cert-manager.yaml

kubectl get pods --namespace cert-manager

# wait for CRD manager
sleep 5
kubectl wait pod -l=app.kubernetes.io/component=webhook --for=condition=Ready --timeout=160s --namespace=cert-manager


kubectl create namespace gemfire-system


helm registry login -u $BROADCOM_MAVEN_USERNAME registry.packages.broadcom.com -p $BROADCOM_MAVEN_PASSWORD


#kubectl create secret docker-registry image-pull-secret --namespace=gemfire-system --docker-server=registry.packages.broadcom.com      --docker-username=$BROADCOM_MAVEN_USERNAME --docker-password=$BROADCOM_MAVEN_PASSWORD
kubectl create secret docker-registry image-pull-secret --namespace=gemfire-system --docker-server=registry.packages.broadcom.com --docker-username=$BROADCOM_MAVEN_USERNAME --docker-password=$BROADCOM_MAVEN_PASSWORD
kubectl create secret docker-registry image-pull-secret --docker-server=registry.packages.broadcom.com --docker-username=$BROADCOM_MAVEN_USERNAME --docker-password=$BROADCOM_MAVEN_PASSWORD

kubectl create rolebinding psp-gemfire --namespace=gemfire-system --clusterrole=psp:vmware-system-privileged --serviceaccount=gemfire-system:default

# Install the GemFire Operator

helm install gemfire-crd oci://registry.packages.broadcom.com/tanzu-gemfire-for-kubernetes/gemfire-crd --version 2.4.0 --namespace gemfire-system --set operatorReleaseName=gemfire-operator
helm install gemfire-operator oci://registry.packages.broadcom.com/tanzu-gemfire-for-kubernetes/gemfire-operator --version 2.4.0 --namespace gemfire-system


sleep 5
kubectl wait pod -l=app.kubernetes.io/component=gemfire-controller-manager --for=condition=Ready --timeout=160s --namespace=gemfire-system


kubectl get pods --namespace gemfire-system


kubectl apply -f https://projectcontour.io/quickstart/contour-gateway-provisioner.yaml

kubectl --namespace projectcontour get deployments

kubectl apply -f deployment/cloud/k8/data-services/gemfire/gf-gateway.yml

kubectl apply -f deployment/cloud/k8/data-services/gemfire/gf-load-balancer.yml

kubectl get services -n kube-system

kubectl apply -f deployment/cloud/k8/data-services/gemfire/gf-load-balance-config-map.yml

#kubectl delete pods -l k8s-app=kube-dns --namespace kube-system