package com.binance.master.utils;

import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.QualityFilter;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;

public class AwsReKognitionUtils {
    private static final Logger logger = LoggerFactory.getLogger(AwsReKognitionUtils.class);

    static AWSCredentials credentials = null;

    // private static final ProfileCredentialsProvider PROFILE_CREDENTIALS_PROVIDER = new
    // ProfileCredentialsProvider();

    static AmazonRekognition rekognitionClient = null;

    /**
     * 
     * @param active 环境变量
     * @param regionName 区域
     */
    public static void init(String active, String regionName) {
        logger.info("环境变量：" + active);
        logger.info("Region：" + regionName);

        Regions region = Regions.fromName(regionName);

        if (active.contains("local")) {

            try {
                credentials = new ProfileCredentialsProvider().getCredentials();
            } catch (Exception e) {
                throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                        + "Please make sure that your credentials file is at the correct "
                        + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
            }

            rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

            logger.info("Local :rekognitionClient init success.");
        } else {
            try {
                if (EcsK8sUtils.isEcs()) {
                    rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                } else if (EcsK8sUtils.isK8s()) {
                    rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(region)
                            .withCredentials(WebIdentityTokenCredentialsProvider.create()).build();
                } else {
                    rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                }
                logger.info("OtherEnv: rekognitionClient init success.");
            } catch (SecurityException e) {
                logger.error(
                        "rekognitionClient init failed, please contact OPS to assign Role ");
            }
        }
    }


    /**
     * 
     * @param collectionId
     * @param photo
     * @return
     */
    public static IndexFacesResult indexFacesByBuffer(String collectionId, ByteBuffer buffer, String photo) throws Exception {

        // Image image = new Image().withS3Object(new
        // S3Object().withBucket(bucket).withName(photo));

        Image image = new Image().withBytes(buffer);

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest().withImage(image)
                .withQualityFilter(QualityFilter.AUTO).withMaxFaces(1).withCollectionId(collectionId)
                .withExternalImageId(photo.replaceAll("/", "_")).withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

        if (indexFacesResult != null) {
            logger.info("Results for " + photo);
            logger.info("Faces indexed:");
            List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
            for (FaceRecord faceRecord : faceRecords) {
                logger.info("  Face ID: " + faceRecord.getFace().getFaceId());
            }
        }

        return indexFacesResult;
    }


    public static IndexFacesResult indexFacesByS3Path(String collectionId, String bucket, String photo)  throws Exception {

        Image image = new Image().withS3Object(new S3Object().withBucket(bucket).withName(photo));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest().withImage(image)
                .withQualityFilter(QualityFilter.AUTO).withMaxFaces(1).withCollectionId(collectionId)
                .withExternalImageId(photo.replaceAll("/", "_")).withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

        // if (indexFacesResult != null) {
        // logger.info("Results for " + photo);
        // logger.info("Faces indexed:");
        // List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        // for (FaceRecord faceRecord : faceRecords) {
        // logger.info(" Face ID: " + faceRecord.getFace().getFaceId());
        // }
        // }

        return indexFacesResult;
    }
    
    public static SearchFacesResult searchFaceMatchingImage(String collectionId, String faceId,
            Float faceMatchThreshold, int maxFaces) throws Exception {

        // ObjectMapper objectMapper = new ObjectMapper();
        // Image image = new Image().withS3Object(new
        // S3Object().withBucket(bucket).withName(photo));

        // Search collection for faces similar to the largest face in the image.
        // SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
        // .withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(70F).withMaxFaces(10);
        //
        // SearchFacesByImageResult searchFacesByImageResult =
        // rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
        SearchFacesRequest re = new SearchFacesRequest().withFaceId(faceId).withCollectionId(collectionId)
                .withMaxFaces(maxFaces).withFaceMatchThreshold(faceMatchThreshold);

        SearchFacesResult searchFaces = rekognitionClient.searchFaces(re);

        // List<FaceMatch> faceMatches = searchFaces.getFaceMatches();
        //
        //
        // for (FaceMatch face : faceMatches) {
        // logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
        // }

        return searchFaces;
    }
    /**
     * 
     * @param collectionId
     * @return
     * @throws Exception
     */
    public static SearchFacesResult searchFaceMatchingImageByFaceId(String collectionId, String faceId,
            Float faceMatchThreshold, int maxFaces) throws Exception {

        // ObjectMapper objectMapper = new ObjectMapper();
        // Image image = new Image().withS3Object(new
        // S3Object().withBucket(bucket).withName(photo));

        // Search collection for faces similar to the largest face in the image.
        // SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
        // .withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(70F).withMaxFaces(10);
        //
        // SearchFacesByImageResult searchFacesByImageResult =
        // rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
        SearchFacesRequest re = new SearchFacesRequest().withFaceId(faceId).withCollectionId(collectionId)
                .withMaxFaces(maxFaces).withFaceMatchThreshold(faceMatchThreshold);

        SearchFacesResult searchFaces = rekognitionClient.searchFaces(re);

        // List<FaceMatch> faceMatches = searchFaces.getFaceMatches();
        //
        //
        // for (FaceMatch face : faceMatches) {
        // logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
        // }

        return searchFaces;
    }
    
    public static SearchFacesByImageResult searchFaceMatchingImageByS3Path(String collectionId, String path,String bucket,
            Float faceMatchThreshold, int maxFaces) throws Exception {

        // ObjectMapper objectMapper = new ObjectMapper();
        // Image image = new Image().withS3Object(new
        // S3Object().withBucket(bucket).withName(photo));

        // Search collection for faces similar to the largest face in the image.
        // SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
        // .withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(70F).withMaxFaces(10);
        //
        // SearchFacesByImageResult searchFacesByImageResult =
        // rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
        
        SearchFacesByImageRequest req = new SearchFacesByImageRequest().withCollectionId(collectionId)
        .withImage(new Image().withS3Object(new S3Object().withBucket(bucket).withName(path))).withMaxFaces(maxFaces).withFaceMatchThreshold(faceMatchThreshold);

        SearchFacesByImageResult searchFacesByImage = rekognitionClient.searchFacesByImage(req);

        // List<FaceMatch> faceMatches = searchFaces.getFaceMatches();
        //
        //
        // for (FaceMatch face : faceMatches) {
        // logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
        // }

        return searchFacesByImage;
    }

    public static SearchFacesByImageResult searchFaceMatchingImageByByteBuffer(String collectionId, byte[] bytebuffer,String bucket,
            Float faceMatchThreshold, int maxFaces) throws Exception {
        
         ByteBuffer buf = ByteBuffer.wrap(bytebuffer);
         Image image = new Image().withBytes(buf);

         SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
         .withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(faceMatchThreshold).withMaxFaces(10);
        
         SearchFacesByImageResult searchFacesByImageResult =
         rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
        

        // List<FaceMatch> faceMatches = searchFaces.getFaceMatches();
        //
        //
        // for (FaceMatch face : faceMatches) {
        // logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
        // }

        return searchFacesByImageResult;
    }
    /**
     * 
     * @param collectionId
     * @param faces
     * @return
     * @throws Exception
     */
    public static DeleteFacesResult deleteFacesFromCollection(String collectionId, String faces[]) throws Exception {
        DeleteFacesRequest deleteFacesRequest =
                new DeleteFacesRequest().withCollectionId(collectionId).withFaceIds(faces);

        DeleteFacesResult deleteFacesResult = rekognitionClient.deleteFaces(deleteFacesRequest);

        // List<String> faceRecords = deleteFacesResult.getDeletedFaces();
        // logger.info(Integer.toString(faceRecords.size()) + " face(s) deleted:");
        // for (String face : faceRecords) {
        // logger.info("FaceID: " + face);
        // }
        return deleteFacesResult;
    }
    
    /**
     * 查看人脸相似度
     * 
     * @return
     * @throws Exception
     */
    public static CompareFacesResult compareFaces(byte[] sourceImage, byte[] targetImage, Float similarityThreshold)
            throws Exception {
        CompareFacesRequest request =
                new CompareFacesRequest().withSourceImage(new Image().withBytes(ByteBuffer.wrap(sourceImage)))
                        .withTargetImage(new Image().withBytes(ByteBuffer.wrap(targetImage)))
                        .withSimilarityThreshold(similarityThreshold).withSdkRequestTimeout(10000);
        CompareFacesResult response = rekognitionClient.compareFaces(request);

        return response;
    }

    /**
     *
     * @param collectionId
     * @param faceId
     * @return
     * @throws Exception
     */
    public static DeleteFacesResult deleteFaceByFaceIds(String collectionId, List<String> faceId) throws Exception {
        DeleteFacesRequest request = new DeleteFacesRequest().withCollectionId(collectionId).withFaceIds(faceId);
        DeleteFacesResult response = rekognitionClient.deleteFaces(request);
        return response;
    }
}
