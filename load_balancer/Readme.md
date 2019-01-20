# Load Balancer

<p>
Load balancing refers to efficiently distributing incoming network traffic across a group of backend servers, also known as a server pool.

Modern high-traffic websites must serve hundreds of thousands of concurrent requests from users or clients and return the correct 
data, all in a fast and reliable manner. 

In this manner, a load balancer performs the following functions:
</p>

	<ul>
	  <li>Distributes client requests or network load efficiently across multiple servers</li>
	  <li>Ensures high availability and reliability by sending requests only to servers that are online</li>
	  <li>Provides the flexibility to add or subtract servers as demand dictates</li>
	</ul>

	
# Load Balancing Algorithms
	The choice of load balancing method depends on your needs:

	<ul>
	  <li>Round Robin – Requests are distributed sequentially  across the group of servers.</li>
	  <li>Least Connections – A new request is sent to the server with the fewest current connections to clients. </li>
	  <li>IP Hash – The IP address of the client is used to determine which server receives the request.</li>
	</ul>

	
<p>
Very general way to balance the load is to use hashing concept. Say we have N servers, we are calculating hash of the request id.
Say, Request id can have values upto K.So, server on which request will be lend is  (k  % N).This will distribute the load on N servers.

The problem with this approach is when:

 We need to scale up machines to add more machines
 Or in case of machine crashes we need to remove machine
 Or assigning a request to a server.
 Or when a cache is mainted to improve performance of the server and due to change in hash request goes to another server, cache is waisted.
	
</p>

To overcome all this we need to use Consistent Hashing

Consistent Hashing maps servers to the key space and assigns requests(mapped to relevant buckets, called load) to the next clockwise server.

Consistent hashing assigns requests to the servers in a way that the load is balanced are remains close to equal.