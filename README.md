Statful Client for Springboot
==============

[![Build Status](https://travis-ci.org/statful/statful-client-springboot.svg?branch=master)](https://travis-ci.org/statful/statful-client-springboot)

Statful client for Springboot. This client is intended to gather and send metrics through [springboot-actuator](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html) and send them to Statful.

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
| 2.x.x | `Java 8` | `2.1.0.RELEASE` |

## Requirements

This client has the following requirements:

* [statful-metric-registry](https://github.com/statful/statful-micrometer-registry) in order to collect system, datasource, http, etc. metrics.
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

You can also define some custom properties for the metrics collected. For examples and detailed explanation on these properties check [statful-metric-registry](https://github.com/statful/statful-micrometer-registry)
 
## Authors

[Mindera - Software Craft](https://github.com/Mindera)

## License

Statful Spring Boot Client is available under the MIT license. See the [LICENSE](https://raw.githubusercontent.com/statful/statful-client-springboot/master/LICENSE) file for more information.
