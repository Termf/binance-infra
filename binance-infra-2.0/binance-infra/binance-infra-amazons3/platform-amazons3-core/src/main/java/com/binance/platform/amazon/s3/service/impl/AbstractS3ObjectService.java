package com.binance.platform.amazon.s3.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.binance.platform.amazon.s3.service.S3ObjectService;
import com.binance.platform.amazon.s3.util.S3ObjectUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.function.Consumer;

@Log4j2
public abstract class AbstractS3ObjectService implements S3ObjectService {

    @Value("${amazon.s3.bucket:}")
    protected String bucketName;

    protected abstract void setAmazonS3(AmazonS3 amazonS3);

    protected abstract AmazonS3 getAmazonS3();

    protected static final int TRAVERSE_OBJECTS_BATCH_SIZE = 10;

    @Override
    public void traverseObjects(String bucketName, String prefix, Consumer<S3ObjectSummary> observer) throws Exception {
        try {
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix)
                .withMaxKeys(TRAVERSE_OBJECTS_BATCH_SIZE);
            ListObjectsV2Result result;
            log.info("traverseObjects for bucketName:{}, prefix: {}", bucketName, prefix);
            do {
                result = getAmazonS3().listObjectsV2(req);
                for (S3ObjectSummary summary : result.getObjectSummaries()) {
                    observer.accept(summary);
                }
                // If there are more than maxKeys keys in the bucket, get a continuation token
                // and list the next objects.
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (RuntimeException e) {
            log.error("listObjects error", e);
            throw new Exception(e);
        }
    }

    @Override
    public void traverseObjects(String prefix, Consumer<S3ObjectSummary> observer) throws Exception {
        traverseObjects(bucketName, prefix, observer);
    }

    @Override
    public String generatePresignedUrl(String objKey, Long expireInSecond) throws Exception {
        log.info("Amazon S3 presign object({}) from bucket({}), expire in {} seconds.", objKey, bucketName,
            expireInSecond);
        try {
            GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, S3ObjectUtils.escapeObjectKey(objKey));
            if (expireInSecond != null) {
                Date expiration = new Date(System.currentTimeMillis() + (expireInSecond * 1000L));
                request.withExpiration(expiration);
            }
            URL url = getAmazonS3().generatePresignedUrl(request);
            return url.toString();
        } catch (RuntimeException e) {
            log.error("Generate presigned url error", e);
            throw new Exception(e);
        }
    }

    @Override
    public String generatePresignedUrl(String bucketName, String objKey, Long expireInSecond) throws Exception {
        log.info("Amazon S3 presign object({}) from bucket({}), expire in {} seconds.", objKey, bucketName,
            expireInSecond);
        try {
            GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, S3ObjectUtils.escapeObjectKey(objKey));
            if (expireInSecond != null) {
                Date expiration = new Date(System.currentTimeMillis() + (expireInSecond * 1000L));
                request.withExpiration(expiration);
            }
            URL url = getAmazonS3().generatePresignedUrl(request);
            return url.toString();
        } catch (RuntimeException e) {
            log.error("Generate presigned url error", e);
            throw new Exception(e);
        }
    }

    @Override
    public boolean existsObject(String objKey) throws Exception {
        try {
            return getAmazonS3().doesObjectExist(bucketName, S3ObjectUtils.escapeObjectKey(objKey));
        } catch (Exception e) {
            log.error("existsObject error", e);
            throw new Exception(e);
        }
    }

    @Override
    public InputStream getObject(String objKey) throws Exception {
        log.info("Amazon S3 get object({}) from bucket({})", objKey, bucketName);
        try {
            S3Object s3Object =
                getAmazonS3().getObject(new GetObjectRequest(bucketName, S3ObjectUtils.escapeObjectKey(objKey)));
            return s3Object.getObjectContent();
        } catch (RuntimeException e) {
            log.error("Get object error, object key: {}", objKey);
            throw new Exception(e);
        }
    }

    @Override
    public void deleteObject(String objKey) throws Exception {
        log.info("Amazon S3 delete object({}) from bucket({})", objKey, bucketName);
        try {
            getAmazonS3().deleteObject(bucketName, S3ObjectUtils.escapeObjectKey(objKey));
        } catch (RuntimeException e) {
            log.error("Delete object error", e);
            throw new Exception(e);
        }
    }

    @Override
    public String generateUploadPresignedUrl(String bucketName, String objKey, Long expireInSecond) throws Exception {
        return generateUploadPresignedUrl(bucketName, objKey, expireInSecond, null);
    }

    @Override
    public String generateUploadPresignedUrl(String bucketName, String objKey, Long expireInSecond,
        CannedAccessControlList acl) throws Exception {
        log.info("Amazon S3  upload presign object({}) from bucket({}), expire in {} seconds.", objKey, bucketName,
            expireInSecond);
        try {
            GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, objKey).withMethod(HttpMethod.PUT);
            if (acl != null) {
                request.addRequestParameter("x-amz-acl", acl.toString());
            }
            if (expireInSecond != null) {
                Date expiration = new Date(System.currentTimeMillis() + (expireInSecond * 1000L));
                request.withExpiration(expiration);
            }
            URL url = getAmazonS3().generatePresignedUrl(request);
            return url.toString();
        } catch (RuntimeException e) {
            log.error("Generate upload presigned url error", e);
            throw new Exception(e);
        }
    }

}
