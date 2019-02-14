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

<h2> Types of Load Balancer: </h2>

<h3> 1) Layer 4 Load balancer: </h3>
        <p>
	In such type of LB, request is forwarded to and from the server without inspecting packets. TCP is the Layer 4                           protocol for HTTP traffic on internet. </p>

<h3> 2) Layer 7 Load Balancer: </h3>
        <p>
	Deals with actual content of each message. HTTP is layer 7 protocol used for website traffic on the internet.                           A Layer 7 load balancer terminates the network traffic and reads the message within. It can make a load-		                 balancing decision based on the content of the message (the URL or cookie, for example). It then makes a new                             TCP connection to the selected upstream server (or reuses an existing one, by means of HTTP keepalives) and                             writes the request to the server. </p>

Let’s look at a simple example. A user visits a high-traffic website. Over the course of the user’s session, he or she might request static content such as images or video, dynamic content such as a news feed, and even transactional information such as order status. Layer 7 load balancing allows the load balancer to route a request based on information in the request itself, such as what kind of content is being requested. So now a request for an image or video can be routed to the servers that store it and are highly optimized to serve up multimedia content. Requests for transactional information such as a discounted price can be routed to the application server responsible for managing pricing.

<h3> 3) DNS Load Balancer: </h3>
	<p> Translates URL to IP. Ex: Google uses it heavily and it receives traffic globally and route it to nearest server. </p>

 <h4> Google uses DNS Load balancer in following steps: </h4>
 
![](https://github.com/tushargoel86/system_design_principle/blob/master/load_balancer/GoogleDNS.jpg)
 
  <ul>
	  <li> User request for URL </li>
	  <li> As Google owns DNS server, so this DNS server request Googel LB to provide IP. Google LB has info of all the connected servers, their running status and their IP.</li>
	  <li> Google LB than request reverse proxy GFE to provide its IP, GFE than provide its IP to the GLB. </li>
	  <li> GLB than returns GFE IP to the customer. </li>
	  <li> Once use request for any particular server, than GFE again asked GLB to provide IP details for the requested server.</li>
	  <li>  GFE than forwards the request on the IP it receives through GLB. Those servers perform the opeerations, and result is returned to the GFE. GFE than forwards this result to GLB and than GLB forwards the result to the user.</li>
</ul>
	
# Load Balancing Algorithms

<p>
The choice of load balancing method depends on your needs:

<ul>
  <li>Round Robin – Requests are distributed sequentially  across the group of servers.</li>
  <li>Least Connections – A new request is sent to the server with the fewest current connections to clients. </li>
  <li>IP Hash – The IP address of the client is used to determine which server receives the request.</li>
</ul>
</p>
	
<p>
Very general way to balance the load is to use hashing concept. Say we have N servers, we are calculating hash of the request id.
Say, Request id can have values upto K.So, server on which request will be lend is  (k  % N).This will distribute the load on N servers.

The problem with this approach is when:
<ul>
<li> We need to scale up machines to add more machines </li>
 <li> Or in case of machine crashes we need to remove machine </li>
 <li> Or assigning a request to a server. </li>
 <li> Or when a cache is mainted to improve performance of the server and due to change in hash request goes to another server, cache is waisted. </li>
</ul>
</p>

To overcome all this we need to use __Consistent Hashing__

__Consistent Hashing__ maps servers to the key space and assigns requests(mapped to relevant buckets, called load) to the next clockwise server. Consistent hashing assigns requests to the servers in a way that the load is balanced are remains close to equal. You can see __implementation__ as well.


<h3> Sticky and Non Sticky sessions: </h3>
  <p> When you use loadbalancing it means you have several instances of tomcat and you need to divide loads. </p>

<h4>If you're using session replication without sticky session :</h4>
	<p>Imagine you have only one user using your web app, and you have 3 tomcat instances. This user sends several requests to your app, then the loadbalancer will send some of these requests to the first tomcat instance, and send some other of these requests to the secondth instance, and other to the third.</p>

<h4>If you're using sticky session without replication : </h4>
	<p>Imagine you have only one user using your web app, and you have 3 tomcat instances. This user sends several requests to your app, then the loadbalancer will send the first user request to one of the three tomcat instances, and all the other requests that are sent by this user during his session will be sent to the same tomcat instance. During these requests, if you shutdown or restart this tomcat instance (tomcat instance which is used) the loadbalancer sends the remaining requests to one other tomcat instance that is still running, BUT as you don't use session replication, the instance tomcat which receives the remaining requests doesn't have a copy of the user session then for this tomcat the user begin a session : the user loose his session and is disconnected from the web app although the web app is still running. </p>

<h4> If you're using sticky session WITH session replication : </h4>
<p>
	Imagine you have only one user using your web app, and you have 3 tomcat instances. This user sends several requests to your 	app, then the loadbalancer will send the first user request to one of the three tomcat instances, and all the other requests that are sent by this user during his session will be sent to the same tomcat instance. During these requests, if you shutdown or restart this tomcat instance (tomcat instance which is used) the loadbalancer sends the remaining requests to one other tomcat instance that is still running, as you use session replication, the instance tomcat which receives the remaining requests has a copy of the user session then the user keeps on his session : the user continue to browse your web app without being disconnected, the shutdown of the tomcat instance doesn't impact the user navigation.
</p>
