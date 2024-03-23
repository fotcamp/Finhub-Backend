package fotcamp.finhub.common.service;

import fotcamp.finhub.admin.dto.request.SaveImgToS3RequestDto;
import fotcamp.finhub.common.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.NoSuchFileException;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Service {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.base-url}")
    private String baseUrl;

    public String uploadFile(SaveImgToS3RequestDto saveImgToS3RequestDto) throws NoSuchFileException {

        if(saveImgToS3RequestDto.getFile().isEmpty()) {
            log.error("image is null");
            throw new NoSuchFileException("파일이 없습니다. 파일을 첨부해주세요.");
        }

        String fileName = getFileName(saveImgToS3RequestDto.getFile());
        // 파일 이름에 type을 포함시켜 폴더 구조를 생성
        String filePath = saveImgToS3RequestDto.getType() + "/" + fileName;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .contentType(saveImgToS3RequestDto.getFile().getContentType())
                    .contentLength(saveImgToS3RequestDto.getFile().getSize())
                    .key(filePath)
                    .build();
            RequestBody requestBody = RequestBody.fromBytes(saveImgToS3RequestDto.getFile().getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            log.error("cannot upload image",e);
            throw new RuntimeException(e);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString() ;
    }

    public String getFileName(MultipartFile multipartFile) {
        return CommonUtils.buildFileName(multipartFile.getOriginalFilename());
    }

    // S3에 저장된 이미지 삭제
    public void deleteImageFromS3(String imageUrl) {
        try {
            // URL에서 키 추출
            URL url = new URL(imageUrl);
            String key = url.getPath().substring(1); // URL의 경로 부분에서 첫 번째 '/' 문자를 제거

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Error deleting image from S3", e);
            throw new RuntimeException("Failed to delete image from S3", e);
        }
    }

    // s3 이미지 base url 빼고 폴더 경로만 return 하게 수정
    public String extractPathFromUrl(String s3Url) throws Exception {
        URL url = new URL(s3Url);
        // 필요한 경우 경로의 특정 부분만 잘라내서 반환
        return url.getPath(); // 예: "/category/test_1710852605205.png"
    }

    // s3 이미지 base url 합쳐서 full url로 return 하게 수정
    public String combineWithBaseUrl(String path) {
        // Base URL과 경로를 결합하여 전체 URL 생성
        return baseUrl + path;
    }
}
