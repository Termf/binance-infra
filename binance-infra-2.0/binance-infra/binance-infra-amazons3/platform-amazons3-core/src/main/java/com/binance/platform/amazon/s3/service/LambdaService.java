package com.binance.platform.amazon.s3.service;

import com.amazonaws.services.lambda.model.InvokeResult;

public interface LambdaService {

    InvokeResult invoke(String function, String payload, int clientExecutionTimeout, int requestTimeout) throws Exception;

}
