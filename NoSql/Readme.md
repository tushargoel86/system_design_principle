## Why NoSQL ?


### Value of Relational Databases
* Getting Persistant data: Allows to store large amount of data
* Concurrency: using transactions.
	* Error Handling. Transaction plays a role in error handling. You can roll back the
						transactions in case of error
* Integration: Shared database integeration. With a single database, several application can
			 share each other's data
* Mostly a standard model: Most of the SQL dialects are similars

#### Problems with Relational Databases

* __Large Scale data:__ Application needs to store and process more data. It's very expensive, if even
					possible to do so with relational database. __This are designed to run on single
					machine.__ So, we can only scale up a machine to certain level. Relational database
					__doesn't support clusters__. Also license are per machine not cluster for them.
					
* __Application Development productivity:__ Lots of time spend on mapping data between in memory DS and
					relational database. This is called __impedance mismatch__.
					
	![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/RelationalDatabase.png)
					

__Impedance mismatch:__ Relational tuples should be simple only. It can't store relatonas (like nested record ot list)
					but in memory data structure can. So to use richer in memory DS you need to translate it to 
					relational representation to store it on disk. Hence 2 different representation that require
					transactions.
<p>					
With the help of object relational mapping frameworks such as Hibernate and iBastis, this problem is solved in many
extent but can still become problem when prople try too hard to ignore the database and query performance suffers.
</p>

#### Emergence of NoSQL
* Not using relational model
* Running well on cluster
* Open Source
* Schemaless
* Use as application database instead of integeration database.


##### NoSQL  helps to develop view of __polygot persistant__
<p> Using different datastore in different circumstances. Instead of using Relational database directly, we need to
understand nature of data we are storing and how we want to manuplate it. We may have mix of data storage technologies 
for different circumstances. </p>

#### Aggregate Data Models:
<p>
A data model is the model through which we perceive and manipulate data.

Relational model takes the information that we want to store and divides it into tuples(row). It capture only set of values
no complex values (like nested list, list of values/tuples within another). It allows us to think all operations as operating on
and return tuples.

Aggregate orientation takes a different approach. Allows us to operate on complex data. Aggregate is a collection of data that we
interact with as a unit. it forms the boundaries for ACID operations with the database.

Aggregate make it eaiser for the database to mange data storage over cluster.
</p>

#### Types of Data Model in NoSQL:
* Key-Value type
* Document based
* Column based Family
* Graph Database
 

__Key Value Data model__:
<p>Key - value based. Search the database using key. Value can be any of type data, images etc.. similar to
Hash Map. In this aggregate is Key. 
</p>

![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/KeyValue.png)

__Document Data Model:__ 
<p> In this model, database as a storage of different documents. Each document is model of different complex datastructure. This datastructure is mostly is JSON or XML. We query the database using document parameter and update portion of it.
Database can create index based on the contents of the aggregates. </p>

![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/Document.png)

__Column DataModel:__ 
<p> More like map of map. Where we want few columns of many rows. </p>

![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/ColumnDatabase.png)

## Distribution Models:

With the large scale data, we need to use clusters of servers which works best for NoSQL. There are 2 types of data distribution:
* Replication: Takes the same data and copies it over multiple nodes. Can be of 2 types:
		* Master Slave
		* Peer to Peer
* Sharding: Puts different data on different nodes.

<p>
We can use Replication and Sharding are orthogonal technologies: You can use either or both of them. 
</p>

#### Single Server: 
<p> In this no distribution to another server. All read and write complexity removed. Genrally NoSQL is designed to use on different machine
but if application is more suited to work on signle machine than we can use Graph NoSQL in this scenario as it is designed to be used on
single machine. </p>

#### Sharding: 
<p>
In this data is distributed on different servers. This distribution is depends upon the aggregates. So aggregates are stored
on different servers but 1 aggregate i stored on 1 machine. This aggregrate can be location specific or order id or anything..
It basically helps to improve read and write performance as different data is stored on different machines and hence can be
accessed separtely.

![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/sharding.PNG)
				
Using replication with cache greatly improves the read performance but doesn't improve write perfromance much.

Sharding helps to scale out horizontally but Sharding on single server reduces resilency as well. That means failure of single
node causing data loss of that shard. </p>

#### Master Slave Replication:
<p>
In this data is replicated on multiple nodes. One node is assiged as Master nodes and rest nodes are Slaves (Secondary) nodes.
Master node is authoriative node and all the write/update operation are performed on this node itself. Replication process
synchronizes the slaves witht the master.

This is mostly helpful for scaling when you have read intensive dataset. We can scale horizontally by adding more nodes to handle
read request and ensuring all read reqeuest are routed to the Slaves.  One of the advantage of this, it provides read resilency.
Should the master failed, slaves can be used for read operations.

But we are still limited with the ability of the master to process updates/write.

![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/Master-Slave.PNG)

As this is limited to ability of Master to perform Write operation, so this is not good approach for write specific operations.
However, having slaves as replicates of Master does speed up the recovery after master failure since a slaves can be appointed 
a new master very quickly.

We need to have different read and write connection so in case of write failure reads are not affected.

Replication also comes with a disadvantage known as inconsistency. Diffrent client reading from different slaves has danger to 
read different values as all changes may not propagate to the slaves. 

Also, it may possible that during update master can failed so no sync between the Slaves and all the data will be lost.
</p>


#### Peer to Peer Replication:
<p>
As Master slaves models does little to provide write specific work (or master failure) Peer to Peer model handle this shortcoming.
In this all the replicas have equal weight, they can accept all writes and the loss of any of them doesn't prevent access to
the datastore.
	
![](https://github.com/tushargoel86/system_design_principle/blob/master/NoSql/images/PeerToPeer.PNG)

We can handle node failure without any data loss and also can add more nodes to improve performance.

Again, with this model we have issue with the consistency. When we write to 2 different places, there is possibility of update 
the same record by 2 different people at same times - a write-write conflict.
</p>

###### We can solve this conflict by 2 ways:
* We can ensure that whenever any write happened it should coordinate with another nodes before write and in case of majority
  decision we can write it. Though this envolves network traffic as a node has to commuicate with all the nodes.
  
* We can come up with a policy to merge inconsistent writes like in git.


##### We can use Sharding and Peer to Peer replication for column family databases.
<p>
Master-slave replication reduces the chance of update conflicts but peer to peer repliccation avoids loading all writes onto a
single point of failure.
</p>

<p> .. More to come...
	
</p>
