package tests.actions.action.concurrent;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tests.BaseRestAssuredTest;
import utils.TokenGenerator;

import java.util.HashMap;
import java.util.Map;

import static helpers.RestHelper.sendPostActionWithoutStatusCheck;
import static helpers.RestHelper.sendPostWithDynamicHeadersAndParams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(utils.LogContextExtension.class)
public class TestAction extends BaseRestAssuredTest {

    @BeforeEach
    public void checkWireMock() {
        for (int i = 0; i < 10; i++) {
            if (wiremockIsRunning) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 400 ошибку и сообщение " +
            "при передаче некорректного token(incorrect token), не существующего в БД")
    public void testActionWithUnknownIncorrectTokenError() {

        Response response = sendPostActionWithoutStatusCheck(ENDPOINT, "incorrect token");

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 400 ошибку и сообщение " +
            "при передаче token, не существующего в БД и соответствующего выражению из ТЗ: \"^[0-9A-Z]{32}$\" ")
    public void testActionWithUnknownAlphaNumericTokenError() {

        Response response = sendPostActionWithoutStatusCheck(ENDPOINT, TokenGenerator.getAlphaNumericToken());

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 400 ошибку и сообщение " +
            "при передаче null token")
    public void testActionWithUnknownNullTokenError() {

        Response response = sendPostActionWithoutStatusCheck(ENDPOINT, null);

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 403 ошибку и сообщение " +
            "при передаче token, не существующего в БД " +
            "и соответствующего выражению из ошибки приложения: \"^[0-9A-F]{32}$\"")
    public void testActionWithUnknownHexadecimalTokenError() {

        String token = TokenGenerator.getHexadecimalToken();
        Response response = sendPostActionWithoutStatusCheck(ENDPOINT, token);

        String expectedMessage = String.format(
                Status.FORBIDDEN_TOKEN_NOT_FOUND.getMessage(),
                token);

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(expectedMessage)
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.FORBIDDEN_TOKEN_NOT_FOUND.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 401 ошибку и сообщение " +
            "при передаче некорректного X-Api-Key")
    public void testActionInvalidXApiKeyError() {

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
                Actions.ACTION,
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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (без LOGIN) возвращает 401 ошибку и сообщение " +
            "при отсутствии передачи X-Api-Key")
    public void testActionMissedXApiKeyError() {

        String token = TokenGenerator.getHexadecimalToken();

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("Content-Type", "application/x-www-form-urlencoded")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT,
                headersMap,
                token,
                Actions.ACTION,
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