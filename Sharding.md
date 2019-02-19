# Sharding

__What is Sharding ?__ 
<p>
Sharding is a concept for paritioning data into chunks using shard key defined by a data modeller (developer). 
This modeller describes range of data involved for a partition.

These chunks are distributed evenly across shards that resides across many several machines.
</p>

#### Why we require partionning??

* __Scale__: needed by modern application to support massive work loads and data volume
* __Geo locality__: To support severs in different geo locations
*	__Hardware Optimization__: For cost effective
* __Lower recovery times__:  For faster recovery

#### Sharding is of 3 types:
* Range
* Hash
* Tag Aware


#### Range Sharding:

<p>
Divide the device id in ranges. Shards are than these ranges...

so example if we have devide id upto 5000 range than we can define shard key

0 --- 1000, 1000 --- 2000, 2000 --- 3000, 3000 --- 4000, 4000 --- 5000

ShardKey : {deviceId}

And per given deviceId, it got store into the shard where it lies. ex: if device id is 2444 so
it comes into 2000 - 3000 range. Hence it will be stored into this range shard.
</p>
