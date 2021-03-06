package com.algaworks.algamoneyapi.exceptionhandle;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;


@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null,LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause().toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));		
		return handleExceptionInternal(ex,erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Erro> erros = criaListaErros(ex.getBindingResult());	
		return handleExceptionInternal(ex,erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> criaListaErros(BindingResult bindResult){
		List<Erro> erros = new ArrayList<>();
		
		for(FieldError fieldError : bindResult.getFieldErrors() ) {
			
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
			
		}
	
		return erros;
	}
	
	public static class Erro{
		
		private String msgUsuario;
		private String msgDesenvolvedor;
		
		public Erro(String msgUsuario,String msgDesenvolvedor) {
			this.msgUsuario = msgUsuario;
			this.msgDesenvolvedor = msgDesenvolvedor;
		}

		public String getMsgUsuario() {
			return msgUsuario;
		}

		public void setMsgUsuario(String msgUsuario) {
			this.msgUsuario = msgUsuario;
		}

		public String getMsgDesenvolvedor() {
			return msgDesenvolvedor;
		}

		public void setMsgDesenvolvedor(String msgDesenvolvedor) {
			this.msgDesenvolvedor = msgDesenvolvedor;
		}
			
	}
}
