package tests.actions.login.concurrent;

import enums.Actions;
import enums.Result;
import enums.Status;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import modelsDTO.ResponseBodyDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tests.BaseRestAssuredTest;
import utils.TokenGenerator;

import java.util.HashMap;
import java.util.Map;

import static helpers.RestHelper.sendPostLoginWithoutStatusCheck;
import static helpers.RestHelper.sendPostWithDynamicHeadersAndParams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(utils.LogContextExtension.class)
public class TestLogin extends BaseRestAssuredTest {

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "Проверка успешного LOGIN с token, соответствующим выражению из ТЗ: \"^[0-9A-Z]{32}$\"")
    public void testLoginWithAlphaNumericTokenSuccess() {

        Response response = sendPostLoginWithoutStatusCheck(ENDPOINT, TokenGenerator.getAlphaNumericToken());

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.OK.name())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.SUCCESS.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "Проверка успешного LOGIN с token, соответствующим выражению из ошибки приложения: \"^[0-9A-F]{32}$\"")
    public void testLoginWithHexadecimalTokenSuccess() {

        Response response = sendPostLoginWithoutStatusCheck(ENDPOINT, TokenGenerator.getHexadecimalToken());

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.OK.name())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.SUCCESS.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "POST запрос для без LOGIN возвращает 400 ошибку и сообщение " +
            "при передаче некорректного token(incorrect token)")
    public void testLoginWithIncorrectTokenError() {

        Response response = sendPostLoginWithoutStatusCheck(ENDPOINT, "incorrect token");

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(Status.BAD_REQUEST_INCORRECT_TOKEN.getMessage())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.BAD_REQUEST_INCORRECT_TOKEN.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "POST запрос для LOGIN возвращает 400 ошибку и сообщение " +
            "при передаче null token")
    public void testLoginWithNullTokenError() {

        Response response = sendPostLoginWithoutStatusCheck(ENDPOINT, null);

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(Status.BAD_REQUEST_MISSING_TOKEN.getMessage())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.BAD_REQUEST_MISSING_TOKEN.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "POST запрос для LOGIN возвращает 401 ошибку и сообщение " +
            "при передаче некорректного X-Api-Key")
    public void testLoginInvalidXApiKeyError() {

        String token = TokenGenerator.getHexadecimalToken();

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("Content-Type", "application/x-www-form-urlencoded"),
                Map.entry("X-Api-Key", "invalidXApiKey")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT,
                headersMap,
                token,
                Actions.LOGIN,
                Status.UNAUTHORIZED.getCode(),
                false);

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(Status.UNAUTHORIZED.getMessage())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.UNAUTHORIZED.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "POST запрос для LOGIN возвращает 401 ошибку и сообщение " +
            "при отсутствии передачи X-Api-Key")
    public void testLoginMissedXApiKeyError() {

        String token = TokenGenerator.getHexadecimalToken();

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("Content-Type", "application/x-www-form-urlencoded")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT,
                headersMap,
                token,
                Actions.LOGIN,
                Status.UNAUTHORIZED.getCode(),
                false);

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(Status.UNAUTHORIZED.getMessage())
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.UNAUTHORIZED.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }
}