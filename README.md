# datty
The same as Netty but for data

### Features
* Based on Netty 4.1x
* Netty ByteBuf is the first citizen for serialization and payloads
* Uses RxJava for async flow, easy to build microservices
* Supports fast data storages: Aerospike, Redis
* Supports big data storages: Cassandra, HBase
* Has own universal Spring Data module [spring-data-datty](spring-data-datty)
* Written with TDD (Test Driven Design), has its own unit artifact [datty-unit](datty-unit). No mocks in your tests.
* Core modules JDK 7 compatible.
