t seems unhappy about the CRD. I bet you had some rabbitmqcluster objects running when you uninstalled?

You likely have RabbitmqCluster objects in terminating state, with a finalizer blocking deletion, because the Operator Pod was deleted before it could remove the finalizers

kubectl get rmq -A will give you all rabbits in all namespaces

If there’s any, very likely in Terminating state, you will have to kubectl edit rmq <name> and remove any finalizer. Then the CRD will be allowed to proceed termination