package pe.gob.minsa.controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.minsa.bean.GenericResponseBean;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionBackRequest {

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public GenericResponseBean HandleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return GenericResponseBean.error(e.getBindingResult().getFieldError().getDefaultMessage());
	}
}
