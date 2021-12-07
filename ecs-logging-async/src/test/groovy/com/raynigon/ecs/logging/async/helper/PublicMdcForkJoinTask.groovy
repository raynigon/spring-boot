package com.raynigon.ecs.logging.async.helper

import com.raynigon.ecs.logging.async.executor.MdcForkJoinTask

import java.util.concurrent.ForkJoinTask

class PublicMdcForkJoinTask extends MdcForkJoinTask {

    PublicMdcForkJoinTask(ForkJoinTask task, Map newContext) {
        super(task, newContext)
    }

    @Override
    Object getRawResult() {
        return super.getRawResult()
    }

    @Override
    void setRawResult(Object value) {
        super.setRawResult(value)
    }

    @Override
    boolean exec() {
        return super.exec()
    }
}