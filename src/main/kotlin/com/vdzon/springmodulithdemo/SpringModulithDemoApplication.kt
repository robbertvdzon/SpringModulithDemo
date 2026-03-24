package com.vdzon.springmodulithdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class SpringModulithDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringModulithDemoApplication>(*args)
}
