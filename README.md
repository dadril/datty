# datty

### Status

Active development

### About

The same as Netty but for data

[datty.io](http://datty.io)

### Features
* Polyglot persistence
* Vendor agnostic
* Multi tenant support
* Multi datacenter support (replicated and partitioned data schemes)
* Multi cloud support (Amazon, Google, etc)
* LDR (failover, active-active)
* DR (one way, active-active)
* Hot migrations support
* Single management console for all datacenters and racks
* Data fusion - merging data from different sources by using field mappings
* Do Yourself API (DYI)
* Models support:
* Indexing of events
* Counters/variables on top of indexes
* Alerting and monitoring
* Rules generation (machine learning)
* Rules simulation (reporting)

### Technologies
* Based on Netty 4.1x
* Netty ByteBuf is the first citizen for serialization and payloads
* Uses simple MessagePack serialization format
* Uses RxJava for async flow, easy to build microservices
* Supports fast data storages: Aerospike, Redis
* Supports big data storages: Cassandra, HBase
* Supports legacy data storages: MySQL, PostgreSQL
* Supports legacy caches: Memcached
* Has own universal Spring Data module [spring-data-datty](spring-data-datty)
* Written with TDD (Test Driven Design), has its own unit artifact [datty-unit](datty-unit). No mocks in your tests.
* Core modules JDK 7 compatible.

