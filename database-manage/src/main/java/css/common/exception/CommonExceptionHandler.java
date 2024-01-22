package css.common.exception;


import css.common.vo.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(TextException.class)
    public ResponseEntity<ResultVo<?>> handleTextException(TextException e) {
        return new ResponseEntity<>(new ResultVo<>(false, e.getMessage(), null), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVo<?>> handleException(Exception e){
        e.printStackTrace();
        return new ResponseEntity<>(new ResultVo<>(false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
