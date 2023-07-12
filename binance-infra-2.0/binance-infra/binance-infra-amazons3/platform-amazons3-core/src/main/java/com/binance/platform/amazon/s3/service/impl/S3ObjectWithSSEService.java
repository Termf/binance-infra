package com.binance.platform.amazon.s3.service.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SSEAlgorithm;
import com.amazonaws.services.s3.model.SSEAwsKeyManagementParams;
import com.binance.platform.amazon.s3.util.S3ObjectUtils;
import com.google.common.io.ByteSource;

import lombok.extern.log4j.Log4j2;


/**
 *
 * S3ObjectService的Server Side Encrption实现。
 * SSE和CSE区别参考 https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/dev/UsingEncryption.html
 *
 * 需要注意，CSE由于是客户端加密，所以使用presigned url get object得到的是加密过的数据，不能直接访问。
 *
 */
@Log4j2
public class S3ObjectWithSSEService extends AbstractS3ObjectService {

    @Resource(name = "AmazonS3")
    private AmazonS3 amazonS3;

    @Value("${amazon.s3.encrypt.key.id:}")
    private String encryptKeyId;



    @Override
    public PutObjectResult putObject(String objKey, byte[] content, String contentType) throws Exception {
        log.info("Amazon S3 put object({}) into bucket({})", objKey, bucketName);
        InputStream contentInputStream = null;
        try {
            contentInputStream = ByteSource.wrap(content).openStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(content.length);
            objectMetadata.setContentType(contentType);
            objectMetadata.setSSEAlgorithm(SSEAlgorithm.KMS.getAlgorithm());

            PutObjectRequest request =
                    new PutObjectRequest(
                            bucketName, S3ObjectUtils.escapeObjectKey(objKey), contentInputStream, objectMetadata
                    ).withCannedAcl(CannedAccessControlList.Private)
                     .withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(encryptKeyId));

            PutObjectResult result = amazonS3.putObject(request);
            log.info("Amazon S3 put object({}) result: {}", objKey, JSON.toJSONString(result));
            return result;
        } catch (RuntimeException e) {
            log.error("Put object to Amazon S3 error", e);
            throw new Exception(e);
        } finally {
            try {
                if (contentInputStream != null) {
                    contentInputStream.close();
                }
            } catch(IOException e) {
                log.error("Close InputStream error. ", e);
            }
        }
    }


    @Override
    public PutObjectResult putObject(String objKey, InputStream inputStream, long length, String contentType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PutObjectResult putObject(String bucket, String objKey, byte[] content, String contentType) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public PutObjectResult putObject(String bucket, String objKey, InputStream inputStream, long length, String contentType) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setAmazonS3(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    protected AmazonS3 getAmazonS3() {
        return amazonS3;
    }
}
