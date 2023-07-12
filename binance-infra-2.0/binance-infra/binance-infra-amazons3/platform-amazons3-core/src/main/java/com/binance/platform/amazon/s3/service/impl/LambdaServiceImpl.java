package com.binance.platform.amazon.s3.service.impl;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.binance.platform.amazon.s3.service.LambdaService;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LambdaServiceImpl implements LambdaService {

    private AWSLambda awsLambda;


    public LambdaServiceImpl(AWSLambda awsLambda){
        this.awsLambda = awsLambda;
    }

    @Override
    public InvokeResult invoke(String function, String payload, int clientExecutionTimeout, int requestTimeout) throws Exception {
        InvokeRequest req = new InvokeRequest()
                .withFunctionName(function)
                .withPayload(payload)
                .withInvocationType(InvocationType.RequestResponse)
                .withSdkClientExecutionTimeout(clientExecutionTimeout)
                .withSdkRequestTimeout(requestTimeout); // optional

        try {
            InvokeResult result = awsLambda.invoke(req);
            return result;
        } catch (Exception e) {
            log.error("invoke aws lambda error", e);
            throw new Exception(e);
        }
    }

}
