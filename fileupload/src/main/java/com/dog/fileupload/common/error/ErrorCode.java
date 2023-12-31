package com.dog.fileupload.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

	OK(200, 200, "성공"),
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),
	NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 1401, "유효하지 않은 토큰입니다.");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
