##This is saga orchestration project. This poc shows saga orchestrator as state machine.

##Overview
In a micro-services architecture, transactions that are within a single service use mainly ACID transactions. The challenge, however, lies in implementing transactions for operations that update state owned by multiple services, and that uses further multiple databases and message brokers. In this case, the application must use an elaborate mechanism to manage transactions. We will also cover how sagas orchestrator can be used as a state machine to manage entity states in a distributed system.

##2PC
The traditional approach to maintain data consistency across multiple services, databases or message brokers is to use distributed transactions and de facto standard for the same is 2PC. 2 PC ensures all participants in a transaction are either commit or rollback. But there are couple of issues lies with this.

*Modern NoSQL databases like MongoDB, Cassandra don't support them. 
*Modern message brokers like Apache Kafka don't support them.
*Synchronous IPC which reduces availability.
*All the participant must be available.

##Saga
Sagas are mechanisms to maintain data consistency and correctness in micro-service architecture. A saga is event driven sequence of local transactions that are coordinated using asynchronous messaging and each local transaction updates state within a single service. Such sequence of local transactions are also called as BASE transactions i.e Basically Available, Soft state, and Eventual consistent. Contrary to ACID, BASE transactions cannot be easily rolled back. To roll back, compensating actions need to be taken to revert anything that has occurred as part of the transaction, called as compensating transactions. Thus, a saga can be think of as high-level business process that consists of several low-level requests that each update data within a single service and each request might have compensating request that is executed when the request fails or the saga is aborted.

##Saga coordination can be implemented in :

*Choreography : Distribute the decision making and sequencing among the saga participants.
*Orchestration : Centralize a saga's coordination logic in a saga orchestrator class. A saga orchetsrator sends commands to saga participants and acts on events outcome.

##Saga Guarantee
A distributed saga guarantees one of the following two outcomes:

Either all requests in the saga are successfully completed, or
A subset of requests and their compensating requests are executed.
both requests and compensating requests need to obey certain characteristics:

requests and compensating requests must be idempotent.
compensating requests must be commutative
requests can abort, which triggers a compensating request. compensating requests cannot abort, they have to execute to completion.

##State machine
A good way to model a saga orchestrator is as a state machine. A state machine consists of a set of states and a set of transitions between states that are triggered by events. Each transition can have an action, which for a saga is the invocation of a saga participant. The transitions between states triggered the completion of a local transaction performed by a saga participant. We will use here saga orchestration approach to define a new service, which can be used as state machine.

##Orchestrator Model
Saga orchestration is built using below stack:

Apache Kafka - event store and distribution.
Axon framework - saga management
State store - in memory
State management - saga driven


