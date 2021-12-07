package com.raynigon.ecs.logging.async.helper

import java.util.concurrent.ForkJoinTask

class DummyForkJoinTask extends ForkJoinTask {

    def result = null

    Runnable runnable = null

    @Override
    Object getRawResult() {
        return result
    }

    @Override
    void setRawResult(Object value) {
        result = value
    }

    @Override
    protected boolean exec() {
        if (runnable != null) {
            runnable.run()
        }
        return true
    }
}