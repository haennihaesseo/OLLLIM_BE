package haennihaesseo.sandoll.global.infra.stt;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import haennihaesseo.sandoll.global.exception.GlobalException;
import haennihaesseo.sandoll.global.status.ErrorStatus;
import haennihaesseo.sandoll.global.util.ResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class GcsClient {

    private final Storage storage;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public GcsClient() {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    ResourceLoader.getResourceAsStream("google-stt-key.json"));
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new GlobalException(ErrorStatus.STT_SERVICE_ERROR);
        }
    }

    public String uploadAudio(byte[] audioBytes, String contentType) {
        String fileName = "stt-audio/" + UUID.randomUUID() + ".webm";

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        storage.create(blobInfo, audioBytes);

        String gcsUri = String.format("gs://%s/%s", bucketName, fileName);
        log.info("[GCS] 오디오 업로드 완료: {}", gcsUri);

        return gcsUri;
    }

    public void deleteAudio(String gcsUri) {
        try {
            String prefix = String.format("gs://%s/", bucketName);
            if (!gcsUri.startsWith(prefix)) {
                return;
            }
            String fileName = gcsUri.substring(prefix.length());
            BlobId blobId = BlobId.of(bucketName, fileName);
            storage.delete(blobId);
            log.info("[GCS] 오디오 삭제 완료: {}", gcsUri);
        } catch (Exception e) {
            log.warn("[GCS] 오디오 삭제 실패: {}", gcsUri, e);
        }
    }
}
