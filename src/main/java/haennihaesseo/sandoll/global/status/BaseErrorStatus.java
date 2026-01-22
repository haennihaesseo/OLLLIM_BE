package haennihaesseo.sandoll.global.status;

import org.springframework.http.HttpStatus;

public interface BaseErrorStatus {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
