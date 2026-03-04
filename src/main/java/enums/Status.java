package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    SUCCESS(200,null),
    CREATED(201,null),
    BAD_REQUEST_INCORRECT_TOKEN(400,"token: должно соответствовать \"^[0-9A-F]{32}$\""),
    BAD_REQUEST_MISSING_TOKEN(400,"token: не должно равняться null"),
    BAD_REQUEST_INVALID_OR_MISSING_ACTION(400,"action: invalid action '%s'. Allowed: LOGIN, LOGOUT, ACTION"),
    UNAUTHORIZED(401,"Missing or invalid API Key"),
    FORBIDDEN_TOKEN_NOT_FOUND(403,"Token '%s' not found"),
    INTERNAL_SERVER_ERROR(500,"Internal Server Error")
    ;

    private final int code;
    private final String message;
}
