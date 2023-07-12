package com.binance.platform.amazon.s3.service;

import java.io.InputStream;
import java.util.function.Consumer;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public interface S3ObjectService {

    /**
     *
     * put new object to amazon s3.
     *
     * @param objKey
     *            unique key to the object
     * @param content
     *            content of the object
     * @return
     * @throws Exception
     */
    PutObjectResult putObject(String bucket, String objKey, byte[] content, String contentType) throws Exception;

    /**
     *
     * put new object to amazon s3.
     *
     * @param objKey
     *            unique key to the object
     * @param content
     *            content of the object
     * @return
     * @throws Exception
     */
    PutObjectResult putObject(String objKey, byte[] content, String contentType) throws Exception;

    /**
     *
     * put new object to amazon s3.
     *
     * @param objKey
     *            unique key to the object
     * @param inputStream
     *            file to be uploaded
     * @param length
     *            size in bytes
     * @return
     * @throws Exception
     */
    PutObjectResult putObject(String bucket, String objKey, InputStream inputStream, long length, String contentType)
        throws Exception;

    /**
     *
     * put new object to amazon s3.
     *
     * @param objKey
     *            unique key to the object
     * @param inputStream
     *            file to be uploaded
     * @param length
     *            size in bytes
     * @return
     * @throws Exception
     */
    PutObjectResult putObject(String objKey, InputStream inputStream, long length, String contentType) throws Exception;

    /**
     *
     * check if object exists.
     *
     * @param objKey
     * @return
     * @throws Exception
     */
    boolean existsObject(String objKey) throws Exception;

    /**
     *
     * get object from amazon s3
     *
     * @param objKey
     *            unique key to the object
     * @return InputStream of content
     * @throws Exception
     */
    InputStream getObject(String objKey) throws Exception;

    /**
     *
     * delete object from amazon s3
     *
     * @param objKey
     *            unique key to the object
     * @throws Exception
     */
    void deleteObject(String objKey) throws Exception;

    /**
     *
     * traverse every object matching prefix.
     *
     * @throws Exception
     */
    void traverseObjects(String prefix, Consumer<S3ObjectSummary> observer) throws Exception;

    /**
     *
     * traverse every object matching prefix.
     *
     * @throws Exception
     */
    void traverseObjects(String bucket, String prefix, Consumer<S3ObjectSummary> observer) throws Exception;

    /**
     *
     * generate public url to the object
     *
     * @param objKey
     * @param expireInSecond
     *            null indicates no expiration
     * @return public accessible url
     * @throws Exception
     */
    public String generatePresignedUrl(String objKey, Long expireInSecond) throws Exception;

    /**
     *
     * generate public url to the object
     *
     * @param objKey
     * @param expireInSecond
     *            null indicates no expiration
     * @return public accessible url
     * @throws Exception
     */
    String generatePresignedUrl(String bucketName, String objKey, Long expireInSecond) throws Exception;

    /**
     *
     * generate upload public url to the object
     *
     * @param objKey
     * @param expireInSecond
     *            null indicates no expiration
     * @return public accessible url
     * @throws Exception
     */
    String generateUploadPresignedUrl(String bucketName, String objKey, Long expireInSecond) throws Exception;

    String generateUploadPresignedUrl(String bucketName, String objKey, Long expireInSecond,
        CannedAccessControlList acl) throws Exception;

}
