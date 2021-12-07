package com.raynigon.ecs.logging.async.helper


import java.time.Duration

class Wait {

    static boolean wait(Duration timeout, Closure<Boolean> action) {
        def start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout.toMillis()) {
            if (action()) {
                return true
            }
            sleep(timeout.toMillis() / 100 as long)
        }
        return false
    }
}
