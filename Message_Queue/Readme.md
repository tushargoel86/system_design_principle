### What is a Message Queue?
<p>
Let's explain the same using the example of Pizza shop.

A new pizza shop is opened in your town. In that customer needs to order and pay to the counter. At the
counter, shop employee provides customer a unique token number. Whenever a new order is ordered a token
number is generated. These orders are processed in inserting order.

Now, in the shop kitchen pizza's are being prepared. A employee there got information about the order and 
start preparing it. Both the front desk and pizza preparing person shares same DS so that it also recieves
order in same order. Once order is completed and employee at front desk deliver to the customer at front desk.

![](https://github.com/tushargoel86/system_design_principle/blob/master/Message_Queue/images/pizzaExample.png)

Now considering, your pizza shop start getting popular and you have opened different branches in city and start
delivering it. 

As you are getting order of the pizza through online, you need to have some center mechanism through which you
can route the request to the nearest center. Also, you need to store all the pending requests to somewhere
(you may need some database here) so that requests can't be lost in case of failure.

Now, as everything goes by plan suddenly one of the shop had some issue and it can't process any order.
What would happened to new order and incomplete order? 
We may directly ignore any new request to this shop but what would happened to incomplete orders?
In such scenario we need to route such request to next nearset shop available.

How do you manage it? Using Load Balancer and Connsistent Hashing.. It distributes all the pending request to all other servers
so as we are using consistent hashing all request goes to same server as it was earlier except new request
which were on faulty shop..

But how does it knows about shop is faulty? Using Heartbeat meachanism. A Centre unit continously sending 
hearbeat to all the server and if no response is recieved in preset time it consider that that shop is down
and distributes it orders to order nearest shops.
</p>

#### So, basically here 3 works are being done here:
* Storing the order and its status (Persisting)
* Distributing the order (Load Balancing)
* Notifiation to check Hearbeat

<p> So, for this Message/Task queue comes into picture. What it does: </p>

* It takes a task
* Store the task
* Distribute the task to the servers
* Wait for the acknowledgement
* If not acknowledgment then its wait till particular time and then it consider that server down and send that request to another server 

##### Example: RabbitMQ, JMS, ZeroMQ

#### How does Message Queue works ?

![](https://github.com/tushargoel86/system_design_principle/blob/master/Message_Queue/images/MessageQueue.png)

* Publishing message means produces message and send to exchange.. Exchange than route the message to queue. 
* Consuming means Consumer than consume this message from the queue itself.
* Producer can’t send message directly to queue.
* Exchange connects to queue using Binding and Binding Key. Each queue has its own binding key.
* To send a message, producer needs to send routing key. Exchange than compare this with binding key as per type of exchange.

#### Types of exchange:


* __Fanout:__  Ignores routing key and forwards message to all the queue.
* __Direct:__  send message to queue having routing key = binding key
* __Top:__     send message to queue which are partially matched.  
* __Header:__  It uses message header instead routing key.
* __Rabbit Q__ has default exchange nameless exchange. Here routing key matches with the queue name.


#### Benefits of using Message Queue:


1.	__Decoupling__ Producers and consumers are independent and can evolve and innovate seperately at their own rate. 
2.	__Redundancy__ Queues can persist messages until they are fully processed.
3.	__Scalability__ Scaling is acheived simply by adding more queue processors. 
4.	__Elasticity & Spikability__ Queues soak up load until more resources can be brought online. 
5.	__Resiliency__ Decoupling implies that failures are not linked. Messages can still be queue even if there's a problem on the consumer side.
6.	__Delivery Guarantees__ Queues make sure a message will be consumed eventually and even implement higher level properties like deliver at most once.
7.	__Ordering Guarantees__ Coupled with publish and subscribe mechanisms, queues can be used message ordering guarantees to consumers.
8.	__Buffering__ A queue acts a buffer between writers and readers. Writers can write faster than readers may read, which helps control the flow of processing through the entire system. 
9.	__Understanding Data Flow__ By looking at the rate at which messages are processed you can identify areas where performance may be improved. 
10.	__Asynchronous Communication__ Writers and readers are independent of each other, so writers can just fire and forget will readers can process work at their own leisure.

