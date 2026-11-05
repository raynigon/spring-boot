package com.raynigon.ecs.logging.access.helper

import com.raynigon.ecs.logging.access.annotation.EcsSkipAccessLogging
import com.raynigon.ecs.logging.access.annotation.EcsSkipBodyLogging
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping
class MultipartAndAnnotationEchoController {

    @PostMapping(path = "multipart", consumes = "multipart/form-data")
    Map<String, Object> uploadMultipart(@RequestPart("file") MultipartFile file, @RequestPart("meta") String meta) {
        return [fileSize: file.size, meta: meta]
    }

    @PostMapping(path = "annotated/skip-body")
    @EcsSkipBodyLogging
    String echoSkipBody(@RequestBody String body) {
        return body
    }

    @PostMapping(path = "annotated/skip-log")
    @EcsSkipAccessLogging
    String echoSkipLog(@RequestBody String body) {
        return body
    }
}
