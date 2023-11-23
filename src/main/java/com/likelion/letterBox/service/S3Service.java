package com.likelion.letterBox.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.likelion.letterBox.DataNotFoundException;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${jwt.secret}")
    private String secretKey;

    public Long getUserId(String email){
        Optional<User> optUser=userRepository.findByEmail(email);
        if(optUser.isEmpty()) throw new DataNotFoundException("유저 정보를 찾을 수 없습니다.");
        else return optUser.get().getId();
    }


    /*
    getURl()을 통해 파일이 저장된 URL을 return 해주고,
    이 URL로 이동 시 해당 파일이 오픈됨(버킷 정책 변경 완료)
     */
    public String saveFile(String directory,String email, MultipartFile multipartFile) throws IOException {
        //TODO: String directory는 Frame이거나 그림일기의 그림이거나 나눠주는 디렉토리
        Long userId=getUserId(email);
        //타임 스탬프로 파일이 계속 덮혀 쓰여지는 것 방지
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String originalFilename = directory+"/"+userId+"/"+timeStamp+"_"+multipartFile.getOriginalFilename();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public String updateFile(String url, MultipartFile multipartFile) throws IOException{

        // 기존 객체 삭제
        if (amazonS3.doesObjectExist(bucket, url)) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, url));
        }
        // 새로운 객체 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, url, multipartFile.getInputStream(), null));
        return amazonS3.getUrl(bucket, url).toString();

    }
    // 이미지 다운로드, 리턴값 변경 필요한지 찾아봐야함.
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }

    // 버킷에 올라간 파일 삭제, 버킷내의 폴더를 두어 할 경우 수정해야할듯
    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }


    //userId로 이미지 저장된거 다 가져오기
    public List<String> findImageUrlsByUserId(String email, String directory) {
        Long userId=getUserId(email);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(directory+"/"+userId + "/");

        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);

        List<String> imageUrls = new ArrayList<>();
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String imageUrl = amazonS3.getUrl(bucket, objectSummary.getKey()).toString();
                imageUrls.add(imageUrl);
            }

            // S3 객체 목록 요청은 페이지네이션될 수 있으므로, 다음 페이지가 있으면 계속 가져옵니다.
            listObjectsRequest.setMarker(objectListing.getNextMarker());
            objectListing = amazonS3.listObjects(listObjectsRequest);
        } while (objectListing.isTruncated());

        return imageUrls;
    }

    public List<String> findStickerImageUrls(String directory, String theme) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(directory+"/"+theme + "/");

        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);

        List<String> imageUrls = new ArrayList<>();
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String imageUrl = amazonS3.getUrl(bucket, objectSummary.getKey()).toString();
                imageUrls.add(imageUrl);
            }

            // S3 객체 목록 요청은 페이지네이션될 수 있으므로, 다음 페이지가 있으면 계속 가져옵니다.
            listObjectsRequest.setMarker(objectListing.getNextMarker());
            objectListing = amazonS3.listObjects(listObjectsRequest);
        } while (objectListing.isTruncated());

        return imageUrls;
    }
}


