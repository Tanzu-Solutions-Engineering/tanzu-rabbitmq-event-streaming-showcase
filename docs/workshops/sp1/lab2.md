

Step 1: Creating a vhost
```
cd /home/ubuntu/rabbitmq-springone2021-workshop/examples/vhosts

kubectl apply -f vhost.yaml
```
##vhost.rabbitmq.com/test-vhost created


Step 2: Creating a user
```
cd /home/ubuntu/rabbitmq-springone2021-workshop/examples/users

kubectl apply -f publish-consume-user.yaml
```
##user.rabbitmq.com/publish-consume-user created

Step 3 : Creating a  lazy Queue

```

cd /home/ubuntu/rabbitmq-springone2021-workshop/examples/queues/

kubectl apply -f lazy-queue.yaml

```

##policy.rabbitmq.com/lazy-queue-policy created

##queue.rabbitmq.com/lazy-queue-example created



Step 4: Creating a quorum Queue
```
cd /home/ubuntu/rabbitmq-springone2021-workshop/examples/queues/

kubectl apply -f quorum-queue.yaml
```
##queue.rabbitmq.com/qq-example created
