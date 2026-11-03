package school.sptech.apl_api_barbershop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BrasilApiIndisponivelException extends RuntimeException {

    public BrasilApiIndisponivelException(String message) {
        super(message);
    }

    public BrasilApiIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}
