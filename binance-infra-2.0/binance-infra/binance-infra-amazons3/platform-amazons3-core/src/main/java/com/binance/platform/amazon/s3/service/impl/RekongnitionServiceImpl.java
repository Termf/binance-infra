package com.binance.platform.amazon.s3.service.impl;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.binance.platform.amazon.s3.service.RekongnitionService;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RekongnitionServiceImpl implements RekongnitionService {

    @Value("${amazon.rekognition.collection.id:}")
    private String rekongnitionCollectionId;

    private AmazonRekognition amazonRekognition;

    public RekongnitionServiceImpl(AmazonRekognition amazonRekognition) {
        this.amazonRekognition = amazonRekognition;
    }

    @Override
    public IndexFacesResult indexFaces(String externalImageId, Integer maxFaces, byte[] image) throws Exception {
        return indexFaces(rekongnitionCollectionId, externalImageId, maxFaces, image);
    }

    @Override
    public IndexFacesResult indexFaces(String collectionId, String externalImageId, Integer maxFaces, byte[] image) throws Exception {
        IndexFacesRequest request = new IndexFacesRequest();
        request.withCollectionId(collectionId)
                .withExternalImageId(externalImageId)
                .withMaxFaces(maxFaces)
                .withImage(
                        new Image().withBytes(ByteBuffer.wrap(image))
                );

        try {
            IndexFacesResult result = amazonRekognition.indexFaces(request);
            return result;
        } catch (RuntimeException e) {
            log.error("indexFaces error", e);
            throw new Exception(e);
        }
    }


}
