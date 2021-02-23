package com.example.oldtown.common.exception;

import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.common.api.ResultCode;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * Created by dyp on 2020/2/27.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }
    //
    // @ResponseBody
    // @ExceptionHandler(value = MethodArgumentNotValidException.class)
    // public CommonResult handleValidException(MethodArgumentNotValidException e) {
    //     BindingResult bindingResult = e.getBindingResult();
    //     String message = null;
    //     if (bindingResult.hasErrors()) {
    //         FieldError fieldError = bindingResult.getFieldError();
    //         if (fieldError != null) {
    //             message = fieldError.getField() + fieldError.getDefaultMessage();
    //         }
    //     }
    //     return CommonResult.validateFailed(message);
    // }
    //
    // @ResponseBody
    // @ExceptionHandler(value = BindException.class)
    // public CommonResult handleValidException(BindException e) {
    //     BindingResult bindingResult = e.getBindingResult();
    //     String message = null;
    //     if (bindingResult.hasErrors()) {
    //         FieldError fieldError = bindingResult.getFieldError();
    //         if (fieldError != null) {
    //             message = fieldError.getField() + fieldError.getDefaultMessage();
    //         }
    //     }
    //     return CommonResult.validateFailed(message);
    // }

    /**
     * 处理 @valid校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = javax.validation.ConstraintViolationException.class)
    @ResponseBody
    public CommonResult constraintViolationExceptionHandler(javax.validation.ConstraintViolationException e) {

        return CommonResult.validateFailed(ResultCode.VALIDATE_FAILED.getMessage() + " : " + e.getMessage());
    }

    /**
     * 处理 @validated校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = org.springframework.validation.BindException.class)
    @ResponseBody
    public CommonResult BindExceptionHandler(org.springframework.validation.BindException e) {

        return CommonResult.validateFailed(ResultCode.VALIDATE_FAILED.getMessage() + " : " + e.getMessage());
    }

    /**
     * 处理 Multipart 参数格式异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = org.springframework.web.multipart.MultipartException.class)
    @ResponseBody
    public CommonResult MultipartExceptionHandler(org.springframework.web.multipart.MultipartException e) {

        return CommonResult.validateFailed(ResultCode.VALIDATE_FAILED.getMessage() + " : " + e.getMessage());
    }

    /**
     * 处理 @RequestParam异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = org.springframework.web.bind.MissingServletRequestParameterException.class)
    @ResponseBody
    public CommonResult MissingExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException e) {

        return CommonResult.validateFailed(ResultCode.VALIDATE_FAILED.getMessage() + " : " + e.getMessage());
    }

    /**
     * 处理 请求格式 错误
     * @param e
     * @return
     */
    @ExceptionHandler(value = org.springframework.security.web.firewall.RequestRejectedException.class)
    @ResponseBody
    public CommonResult RejectExceptionHandler(org.springframework.security.web.firewall.RequestRejectedException e) {

        return CommonResult.validateFailed(ResultCode.VALIDATE_FAILED.getMessage() + " : " + e.getMessage());
    }

    /**
     * 处理 外键校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseBody
    public CommonResult dataIntegrityViolationExceptionHandler(org.springframework.dao.DataIntegrityViolationException e) {

        return CommonResult.validateFailed("外键校验异常: " + e.getMessage());
    }

}
