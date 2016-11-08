Statful Client for Springboot
==============

[![Build Status](https://travis-ci.org/statful/statful-client-springboot.svg?branch=master)](https://travis-ci.org/statful/statful-client-springboot)

Statful client for Springboot. This client is intended to gather metrics provided by [springboot-actuator](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html) and send them to Statful.

> At this point only system, tomcat and http metrics are collected but support for datasource and cache metrics will be added soon.

## Table of Contents

* [Supported Versions](#supported-versions)
* [Requirements](#requirements)
* [Quick Start](#quick-start)
* [Examples](#examples)
* [Reference](#reference)
* [Authors](#authors)
* [License](#license)

## Supported Versions

| Statful client version | Tested Java versions  | Tested Spring Boot versions
|:---|:---|:---|
| 1.x.x | `Java 8` | `1.4.1.RELEASE` |

## Requirements

This client has the following requirements:

* [springboot-actuator](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-actuator) in order to collect system, datasource, http, etc. metrics.
* [statful-client-java](https://github.com/statful/statful-client-java) in order to send metrics into Statful.

## Quick start

This client requires you to configure and initialize a `StatfulClient` bean as described [here](https://github.com/statful/statful-client-java#quick-start).  

After that simply add the dependency using Maven for example:

```
<dependency>
    <groupId>com.statful.client.framework</groupId>
    <artifactId>client-springboot</artifactId>
    <version>${statful-client-springboot.version}</version>
</dependency>
```

And enable `@ComponentScan` for the `com.statful.client.framework.springboot` base package.

Enable metric collection by setting the following property:

```
statful.client.springboot.metrics.enabled=true
```

> **IMPORTANT:** This client partially uses the `StatfulClient` configuration. Although it doesn't reuse the global tags, aggregations or namespaces. Those can be set via application properties as described below.

## Examples

You can see the metrics output by running the client in dry-run mode, it will look similar to:

```
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.mem,type=total 535284.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.mem,type=free 436612.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.processors 8.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.uptime,type=instance 47148.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.uptime,type=vm 53309.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.systemload 4.0888671875 1476908774549 Aggregation: AVG Frequency: FREQ_60
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.heap,type=committed 479744.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.heap,type=init 262144.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.heap,type=used 43131.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.heap,type=max 3728384.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.nonheap,type=committed 57344.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.nonheap,type=init 2496.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.nonheap,type=used 55540.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.nonheap,type=max 0.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.threads,type=peak 22.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.threads,type=daemon 19.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.threads,type=totalStarted 26.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.threads,type=total 22.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.classes,type=total 6614.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.classes,type=loaded 6615.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.system.classes,type=unloaded 1.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.counter.system.accumulated.gc,name=ps_scavenge 8.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.timer.system.accumulated.gc,name=ps_scavenge 97.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.counter.system.accumulated.gc,name=ps_marksweep 2.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.timer.system.accumulated.gc,name=ps_marksweep 141.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.tomcat.httpsessions,type=max -1.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.gauge.tomcat.httpsessions,type=active 0.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.timer.latest.responses,url=metrics 4.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.timer.latest.responses,url=star-star.favicon.ico 7.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.counter.accumulated.requests,url=star-star.favicon.ico,status=200 6.0 1476908774549
INFO 57428 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: springboot.counter.accumulated.requests,url=metrics,status=200 5.0 1476908774549
```

### Extra Configuration

You can get an extra level of customization by setting the namespace and prefix of your metrics.

For that set the following application properties:

```
statful.client.springboot.metrics.namespace=example
statful.client.springboot.metrics.prefix=springboot
```

A list of tags to be added across all collected metrics can be configure with the following properties:

```
statful.client.springboot.metrics.tags[0].name=application
statful.client.springboot.metrics.tags[0].value=starter
statful.client.springboot.metrics.tags[1].name=framework
statful.client.springboot.metrics.tags[1].value=springboot
```

An example of the output this configuration would generate is:

```
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.mem,framework=springboot,application=starter,type=total 439643.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.mem,framework=springboot,application=starter,type=free 272999.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.processors,application=starter,framework=springboot 8.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.uptime,framework=springboot,application=starter,type=instance 26554.01476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.uptime,framework=springboot,application=starter,type=vm 31126.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.systemload,application=starter,framework=springboot 3.005859375 1476909489668 Aggregation: AVG Frequency: FREQ_60
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.heap,framework=springboot,application=starter,type=committed 385536.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.heap,framework=springboot,application=starter,type=init 262144.0 1476909489668
INFO 57755 --- [pool-2-thread-1] c.s.c.core.sender.BufferedMetricsSender  : Dry metric: example.springboot.gauge.system.heap,framework=springboot,application=starter,type=used 112536.0 1476909489668
```

> In order to be able to define metric tags as above the springboot [configuration-metadata-annotation-processor](http://docs.spring.io/spring-boot/docs/1.4.1.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor) is used. If you're using AspectJ in your project please refer to the documentation on the expected annotation processor behaviour.

### Control Processors

You can enable/disable all metric processors by setting the following properties:
 
```
statful.client.springboot.processors.tomcat.httpSessions.enabled=true
statful.client.springboot.processors.http.httpRequests.enabled=true
statful.client.springboot.processors.system.classes.enabled=true
statful.client.springboot.processors.system.gc.enabled=true
statful.client.springboot.processors.system.heap.enabled=true
statful.client.springboot.processors.system.mem.enabled=true
statful.client.springboot.processors.system.processors.enabled=true
statful.client.springboot.processors.system.systemload.enabled=true
statful.client.springboot.processors.system.threads.enabled=true
statful.client.springboot.processors.system.uptime.enabled=true
```

All processors are enabled by default.

## Reference

### Collected Metrics

Metrics are collected according to what's described in the [Spring Boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html) and modelled into Statful format with the following considerations:

* The type of metric is inferred by the metric's meaning with is decided both from the Spring Boot documentation and the actual code that exports data
* The majority of metrics are `gauges` and represent a particular value collected in an instant
* Times such as `gc` collection time or `http` response time are modelled as a `timers`
* Counts such as `gc` collection count or `http` request count are modelled as `counters`
* Some metrics represent an accumulated value over time, these are prefixed with `accumulated` on the metric name
* Some metrics represent the last measured value for a particular entry, these are prefixed with `latest` on the metric name
* Metrics that represent a value aggregated over a period of time are sent to Statful as previously aggregated
 
> Note that aggregated metrics are currently only supported by using the HTTP transport on the `StatfulClient`.

## Authors

[Mindera - Software Craft](https://github.com/Mindera)

## License

Statful Spring Boot Client is available under the MIT license. See the [LICENSE](https://raw.githubusercontent.com/statful/statful-client-springboot/master/LICENSE) file for more information.
