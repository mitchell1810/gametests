package tests.actions.login;

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
import tests.BaseWireMockTest;
import utils.TokenGenerator;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static helpers.RestHelper.sendPostLoginWithoutStatusCheck;
import static helpers.RestHelper.sendPostWithDynamicHeadersAndParams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@ExtendWith(utils.LogContextExtension.class)
public class TestLoginWithImitationOfLogin extends BaseWireMockTest {

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "LOGIN")
    @Story(value = "POST запрос для LOGIN выполняется при отсутствии заголовка 'Content-Type'")
    public void testLoginMissedContentTypeSuccessWithMockedLogin() {

        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.LOGIN, Status.SUCCESS.getCode(), false);

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
    @Story(value = "POST запрос для LOGIN выполняется при отсутствии заголовка 'Accept'")
    public void testLoginMissedAcceptSuccessWithMockedLogin() {

        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Content-Type", "application/x-www-form-urlencoded"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.LOGIN, Status.SUCCESS.getCode(), false);

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
    @Story(value = "Проверка успешного действия LOGIN c использованием моков")
    public void testLoginWithAlphaNumericTokenSuccessWithMockedLogin() {

        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

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
    @Story(value = "Проверка среднего времени ответа у 10 последовательных успешных POST запросов для действия LOGIN " +
            "на соответствие условному среднему отличному результату (меньше 200мс) " +
            "c использованием моков для LOGIN действия")
    public void testLoginAverageResponseTimeSuccessWithMockedLogin() {

        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        long sumTime = 0;
        for (int i = 0; i < REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS; i++) {
            sumTime += sendPostLoginWithoutStatusCheck(ENDPOINT, TokenGenerator.getHexadecimalToken())
                    .getTime();
        }
        long actualAverageTime = sumTime / REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS;

        assertTrue(EXPECTED_AVERAGE_TIME > actualAverageTime,
                "Actual average response time " + actualAverageTime + " higher than expected " + EXPECTED_AVERAGE_TIME);
    }
}