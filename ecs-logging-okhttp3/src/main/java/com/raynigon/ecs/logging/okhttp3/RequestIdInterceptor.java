package com.raynigon.ecs.logging.okhttp3;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_HEADER;
import static com.raynigon.ecs.logging.LoggingConstants.TRANSACTION_ID_PROPERTY;

class RequestIdInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        String transactionId = MDC.get(TRANSACTION_ID_PROPERTY);
        // If no transaction id exists, generate custom transaction id
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
            MDC.put(TRANSACTION_ID_PROPERTY, transactionId);
        }
        // Update Request with transaction id header
        Request updated = original.newBuilder()
                .header(TRANSACTION_ID_HEADER, transactionId)
                .build();
        return chain.proceed(updated);
    }
}