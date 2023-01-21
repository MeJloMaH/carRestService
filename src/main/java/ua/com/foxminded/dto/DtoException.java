package ua.com.foxminded.dto;

public class DtoException extends Exception {

	private static final long serialVersionUID = 6564217478150777788L;

	public DtoException(String errorMessage) {
		super(errorMessage);
	}
}
