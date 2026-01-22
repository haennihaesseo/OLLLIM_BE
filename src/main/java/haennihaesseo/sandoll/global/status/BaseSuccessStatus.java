package haennihaesseo.sandoll.global.status;

import org.springframework.http.HttpStatus;

public interface BaseSuccessStatus {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}