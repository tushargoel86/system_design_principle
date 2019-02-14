<p>
Load Balancer help us to scale horizontally but caching will enable us to make vastly better us of the resources we already have.

Cache can exist at all level of architecture.
</p>

<h5> Different types of cache: </h5>
__Application server cache:__
  <ul>
	<li>	Cache directly on Application server </li>
	<li>	On each request data is fetched from the cache available on App server </li>
	<li>	In case of cache miss, data is fetched from the database over the n/w </li>
  </ul>

__But how do you scale it?__
<p> To scale this approach we need to have separate cache on each App server. However if LB distributes the requests than each request may goes to different servers thus increasing cache miss.</p>

__Impact:__
* Extra n/w request to fetch data
* Duplicate data
* May cause inconsistent application behavior. How we handle consistency and Cache invalidation for each node.

<p>There are 2 solutions for this problem: </p>

__Global cache:__
* All nodes using same cache
* Each node query the cache as it would be local one
* Can be in effective for large application but could be effective for small application

__Distributed cache:__
* Cache is divided up using Consistent Hashing function and each of its node own part of the cached data
* If a request node is looking for a certain piece of data, it can quickly use to hashing function to locate the information within the distributed cache to determine if the data is available. 	
* By simply adding nodes into Consistent Hashing we can increase or reduce cache size.

<p>If a request node is looking for a certain piece of data, it can quickly use to hashing function to locate the information within the distributed cache to determine if the data is available. </p>

__Content Distribution Network (CDN)__
* This is the best solution if our sites serving large amounts of static media
* We can serve static media off separate subdomain using a lightweight HTTP server like apache and cutover the DNS from your servers to a CDN layer.

__How typical CDN works?__
* A request will ask the CDN for a piece of static data which is a media.
* The CDN will serve the content if it is available locally.
* If it is not available, the CDN will query the back-end servers for the media and cache it locally.
* Then it serves the media to the requesting client.

__Caching strategy is depend upon the data and data access pattern (how data is read and written).__
* Is heavy system write and in frequent read (logs)
* Is heavy system read and in frequent write (user profile)
* Is unique data returned (search query)

__Cache Aside:__
* Application first check data into cache, If found return directly from the cache and If not found than read it from the database,   return to the customer and update the cache

* Work best for read heavy application. Ex: Memcache, Redis
* In case of cache failure, request directly goes to the database
* Another benefit is that the data model in cache can be different than the data model in database

* When cache-aside is used, the most common write strategy is to write data to the database directly. When this happens, cache may become inconsistent with the database. To deal with this, developers generally use time to live (TTL) and continue serving stale data until TTL expires. If data freshness must be guaranteed, developers either invalidate the cache entry or use an appropriate write strategy


__Read-Through Cache__

* Read-through cache sits in-line with the database. When there is a cache miss, it loads missing data from database, populates the cache and returns it to the application.

<p>
Both cache-aside and read-through strategies load data lazily, that is, only when it is first read. 

While read-through and cache-aside are very similar, there are at least two key differences:
1.	In cache-aside, the application is responsible for fetching data from the database and populating the cache. In read-through, this logic is usually supported by the library or stand-alone cache provider.
2.	Unlike cache-aside, the data model in read-through cache cannot be different than that of the database.
Read-through caches work best for read-heavy workloads when the same data is requested many times. For example, a news story. The disadvantage is that when the data is requested the first time, it always results in cache miss and incurs the extra penalty of loading data to the cache. Developers deal with this by ‘warming’ or ‘pre-heating’ the cache by issuing queries manually. Just like cache-aside, it is also possible for data to become inconsistent between cache and the database, and solution lies in the write strategy.

</p>
 
__Write-Through Cache__
<p>In this write strategy, data is first written to the cache and then to the database. The cache sits in-line with the database and writes always go through the cache to the main database.
 
Introduce extra write latency because data is written to the cache first and then to the main database.  Can combine with read through cache. Write is confirmed as success only if writes to DB and the cache BOTH succeed. We will have complete data consistency between cache and storage
</p>

__Write-Around__
<p>
Here, data is written directly to the database and only the data that is read makes it way into the cache. However, it increases cache misses because the cache system reads the information from DB incase of a cache miss. As a result of it, this can lead to higher read latency incase of applications which write and re-read the information quickly. Read must be happened from slower back-end storage and experience higher latency.
Write-around can be combine with read-through and provides good performance in situations where data is written once and read less frequently or never. For example, real-time logs or chatroom messages. Likewise, this pattern can be combined with cache-aside as well.
</p>

__Write-Back__
<p> Here, the application writes data to the cache which acknowledges immediately and after some delay, it writes the data back to the database. Write back caches improve the write performance and are good for write-heavyworkloads.

When combined with read-through, it works good for mixed workloads, where the most recently updated and accessed data is always available in cache. It’s resilient to database failures and can tolerate some database downtime. 

Some developers use Redis for both cache-aside and write-back to better absorb spikes during peak load. The main disadvantage is that if there’s a cache failure, the data may be permanently lost.
</p>

__Cache Eviction policy:__
FIFO, LIFO, LRU, MRU, LFU, RR (Random Replacement)
