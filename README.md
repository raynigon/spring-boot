# Raynigon Spring Boot Libraries
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/967b93564acf4b55811b08d9544b44a6)](https://app.codacy.com/gh/raynigon/spring-boot?utm_source=github.com&utm_medium=referral&utm_content=raynigon/spring-boot&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/0764696c15a941c78bef58fef5082d06)](https://www.codacy.com/gh/raynigon/spring-boot/dashboard?utm_source=github.com&utm_medium=referral&utm_content=raynigon/spring-boot&utm_campaign=Badge_Coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.raynigon.spring-boot/gzip-request-filter-starter/badge.svg)](https://search.maven.org/search?q=com.raynigon.spring-boot)

This repository contains some usefull libraries which can enhance your spring boot experience.

## ECS Logging
The ecs-logging-* libraries provide the functionality to log messages as json documents in the [ECS format](https://www.elastic.co/guide/en/ecs/current/index.html).

## ECS Logging - App
This library provides application logs in ECS format.

## ECS Logging - Access
This library provides access log functionality for tomcat.

## ECS Logging - Audit
This library provides audit log functionality for the application.
These loggers need to be managed manually.

## ECS Logging - Async
This library provides the functionality of the ECS application logs,
for asynchronous processes (e.g. futures, or @Async annotated methods).

## Gzip Request Filter
The gzip-request-filter enabled the processing of gzip compressed requests.
E.g. Post requests with much data.
