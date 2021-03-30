// Copyright 2021 ALO7 Inc. All rights reserved.

package com.kael.example.exception;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

/**
 * @author Kael He (kael.he@alo7.com)
 */
public class CustomFatalExceptionStrategy
        extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
    @Override
    public boolean isFatal(Throwable t) {
        return t.getCause() instanceof Exception;
    }
}