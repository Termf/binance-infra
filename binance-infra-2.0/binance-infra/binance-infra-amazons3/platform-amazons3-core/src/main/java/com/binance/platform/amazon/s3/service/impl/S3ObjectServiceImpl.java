package com.binance.platform.amazon.s3.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.binance.master.utils.JsonUtils;
import com.binance.platform.amazon.s3.util.S3ObjectUtils;
import com.google.common.io.ByteSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 *
 * S3ObjectServiceImpl without encryption
 *
 */
@Log4j2
public class S3ObjectServiceImpl extends AbstractS3ObjectService {

    @Resource(name = "AmazonS3")
    private AmazonS3 amazonS3;

    @Override
    public PutObjectResult putObject(String objKey, byte[] content, String contentType) throws Exception {
        return putObject(bucketName, objKey, content, contentType);
    }

    @Override
    public PutObjectResult putObject(String bucket, String objKey, byte[] content, String contentType)
        throws Exception {
        log.info("Amazon S3 put object({}) into bucket({})", objKey, bucket);
        InputStream contentInputStream = null;
        try {
            contentInputStream = ByteSource.wrap(content).openStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(content.length);
            PutObjectRequest request =
                new PutObjectRequest(bucket, S3ObjectUtils.escapeObjectKey(objKey), contentInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.Private);

            PutObjectResult result = amazonS3.putObject(request);
            log.info("Amazon S3 put object({}) result: {}", objKey, JsonUtils.toJsonHasNullKey(result));
            return result;
        } catch (RuntimeException e) {
            log.error("Put object to Amazon S3 error", e);
            throw new Exception(e);
        } finally {
            try {
                if (contentInputStream != null) {
                    contentInputStream.close();
                }
            } catch (IOException e) {
                log.error("Close InputStream error. ", e);
            }
        }
    }

    @Override
    public PutObjectResult putObject(String bucket, String objKey, InputStream inputStream, long length,
        String contentType) throws Exception {
        if (StringUtils.isEmpty(objKey) || inputStream == null || length <= 0L) {
            log.warn("objKey is empty or inputStream is null");
            return null;
        }

        log.info("Amazon S3 put object({}) into bucket({})", objKey, bucket);
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(length);
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentType(Mimetypes.getInstance().getMimetype(objKey));

            PutObjectRequest request =
                new PutObjectRequest(bucket, S3ObjectUtils.escapeObjectKey(objKey), inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.Private);

            PutObjectResult result = amazonS3.putObject(request);
            log.info("Amazon S3 put object({}) result: {}", objKey, JsonUtils.toJsonHasNullKey(result));
            return result;
        } catch (AmazonServiceException e) {
            log.error("Put object to Amazon S3 error", e);
            throw new Exception(e);
        } catch (SdkClientException e) {
            log.error("Put object to Amazon S3 error, couldn't get response, ", e);
            throw new Exception(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Close InputStream error:", e);
            }
        }
    }

    @Override
    public PutObjectResult putObject(String objKey, InputStream inputStream, long length, String contentType)
        throws Exception {
        return putObject(bucketName, objKey, inputStream, length, contentType);
    }

    @Override
    protected void setAmazonS3(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    protected AmazonS3 getAmazonS3() {
        return this.amazonS3;
    }

}