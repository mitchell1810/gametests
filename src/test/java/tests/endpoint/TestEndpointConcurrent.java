package tests.endpoint;

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

import static helpers.RestHelper.getCorrectHeaders;
import static helpers.RestHelper.sendPostWithDynamicHeadersAndParams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(utils.LogContextExtension.class)
public class TestEndpointConcurrent extends BaseRestAssuredTest {

    @Test
    @Epic(value = "Проверка endpoint")
    @Feature(value = "endpoint")
    @Story(value = "Проверка POST запроса c некорректным action о возврате 400 ошибки с сообщением " +
            "'action: invalid action 'INVALID_ACTION'. Allowed: LOGIN, LOGOUT, ACTION'")
    public void testInvalidActionError() {
        Response response = sendPostWithDynamicHeadersAndParams(
                "/endpoint",
                getCorrectHeaders(),
                TokenGenerator.getHexadecimalToken(),
                Actions.INVALID_ACTION,
                Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getCode(),
                false);

        String expectedMessage = String.format(
                Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getMessage(),
                Actions.INVALID_ACTION.name());

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(expectedMessage)
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }

    @Test
    @Epic(value = "Проверка endpoint")
    @Feature(value = "endpoint")
    @Story(value = "Проверка POST запроса с 'null' action о возврате 400 ошибки с сообщением " +
            "'action: invalid action 'null'. Allowed: LOGIN, LOGOUT, ACTION'")
    public void testMissedActionError() {

        Response response = sendPostWithDynamicHeadersAndParams(
                "/endpoint",
                getCorrectHeaders(),
                TokenGenerator.getHexadecimalToken(),
                null,
                Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getCode(),
                false);

        String expectedMessage = String.format(
                Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getMessage(),
                "null");

        ResponseBodyDTO actualResponse = convertJsonDataToResponseBodyDTO(response.getBody().prettyPrint());
        ResponseBodyDTO expectedResponse = ResponseBodyDTO.builder()
                .result(Result.ERROR.name())
                .message(expectedMessage)
                .build();

        Assertions.assertAll(
                () -> assertEquals(expectedResponse, actualResponse,
                        "Тело актуального ответа не соответствует телу ожидаемого"),
                () -> assertEquals(Status.BAD_REQUEST_INVALID_OR_MISSING_ACTION.getCode(), response.getStatusCode(),
                        "Status code актуального ответа не соответствует Status code ожидаемого")
        );
    }
}