package com.nextrt.acm.config.exception;

import com.nextrt.core.vo.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 验证异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) throws MethodArgumentNotValidException {
        Result result = new Result();
        BindingResult bindingResult = e.getBindingResult();
        result.setStatus(-1);

        StringBuilder msg = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            msg.append(fieldError.getDefaultMessage()).append(",");
        }
        result.setMsg(msg.toString());
        return result;
    }

    /**
     * 全局异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        Result result = new Result();
        result.setStatus(-1);
        result.setMsg("系统发生错误");
        result.setData(e.getMessage());
        return result;
    }
}
