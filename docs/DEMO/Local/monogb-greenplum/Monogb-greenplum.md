Setup

Start MongoDB server

```shell
export MONGODB_VERSION=6.0-ubi8
docker  network create tanzu
docker run --network=tanzu --name mongodb -it --rm  -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=mongo  -e MONGO_INITDB_ROOT_PASSWORD=mongo mongodb/mongodb-community-server:$MONGODB_VERSION
```


Mongosh

```shell
export MONGODB_VERSION=6.0-ubi8
docker run --network=tanzu  -it mongodb/mongodb-community-server:$MONGODB_VERSION mongosh "mongodb://mongo:mongo@mongodb/admin"
#docker run --network=tanzu  -it mongodb/mongodb-community-server:$MONGODB_VERSION mongosh "mongodb://mongo:mongo@mongodb/local"
```


```shell
show dbs
use admin
db.user.insert({"_id": "joedoe", name: "Joe Doe", age: 45, synced: false})
db.user.insert({"_id": "jilldoe", name: "Jill Doe", age: 50, synced: false})
db.user.insert({"_id": "jsmith", name: "John Smith", age: 33, synced: false})
db.user.insert({"_id": "jacme", name: "James Acme", age: 33, synced: false})
```

Delete

```shell
db.user.deleteMany({})
```

List of collections
```shell
db.getCollectionNames()
```

Query

{ age : { $lt : 0 }, accounts.balance : { $gt : 1000.00 }

```shell
db.user.find({ synced : { $eq :  false }})
```

```shell
db.user.find({})
```


```shell
db.user.updateMany({synced: true}, {$set:{synced: false}})  
```


```shell
db.user.updateMany({synced: true, "_id": "joedoe" }, {$set:{synced: false, age: 66}})  
```

```shell

gti 
```

query


------------------------

Greenplum

```sql
DROP TABLE app_users;

CREATE TABLE app_users (
	id varchar NOT NULL,
	name varchar NOT NULL,
	age int NULL,
	CONSTRAINT app_users_id_pk PRIMARY KEY (id)
);
```

---------
# SCDF



```shell
mongo-stream=mongodb --username=mongo --database=admin --password=mongo --host=localhost --collection=user --query="{ synced : { $eq :  'false' }" --update-expression="{synced: true}" | greenplum: jdbc --columns="name,age" --table-name=app_users --driver-class-name=org.postgresql.Driver--url="" --username= --password=
```

```properties
app.mongodb.update-expression="'{$set: { synced: true }}'"
app.mongodb.query="'{ synced : { $eq :  false }'"
```


```shell
db.user.updateMany({synced: true, "_id": "jsmith" }, {$set:{synced: false, name: "Johnson Smith"}})  
```
