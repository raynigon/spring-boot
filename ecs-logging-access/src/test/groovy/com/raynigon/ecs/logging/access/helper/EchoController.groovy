package com.raynigon.ecs.logging.access.helper

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class EchoController {

    @GetMapping("/")
    String home() {
        return "Hello World!"
    }

    @PostMapping("echo")
    String echo(@RequestBody String body) {
        return body
    }
}
