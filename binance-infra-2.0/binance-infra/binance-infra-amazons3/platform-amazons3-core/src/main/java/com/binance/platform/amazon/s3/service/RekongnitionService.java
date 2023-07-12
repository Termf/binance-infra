package com.binance.platform.amazon.s3.service;

import com.amazonaws.services.rekognition.model.IndexFacesResult;

public interface RekongnitionService {

    /**
     *
     * index faces in S3 object to default collection
     *
     * @param externalImageId
     * @param maxFaces
     * @param image
     * @return
     * @throws Exception
     */
    IndexFacesResult indexFaces(String externalImageId, Integer maxFaces, byte[] image) throws Exception;

    /**
     *
     * index faces in S3 object to specified collection
     *
     * @param externalImageId
     * @param maxFaces
     * @param image
     * @return
     * @throws Exception
     */
    IndexFacesResult indexFaces(String collectionId, String externalImageId, Integer maxFaces, byte[] image) throws Exception;

}
