package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* User의 경우 1000번대 에러코드 사용
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{
    USER_NOT_FOUND(400,1404,"사용자 찾을 수 없음"),
    AUTHENTICATION_ERROR(401,1405,"권한이 없음"),
    PASSWORD_ERROR(400,1406 ,"비밀 번호 틀림" );
    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
