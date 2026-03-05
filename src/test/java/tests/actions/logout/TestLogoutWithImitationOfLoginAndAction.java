package tests.actions.logout;

import enums.Actions;
import enums.Result;
import enums.Status;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Features;
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
import static helpers.RestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@ExtendWith(utils.LogContextExtension.class)
public class TestLogoutWithImitationOfLoginAndAction extends BaseWireMockTest {

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Features(value = {@Feature(value = "LOGOUT"), @Feature(value = "US-CASE")})
    @Story(value = "POST запрос для LOGOUT (после LOGIN с сохранением token) выполняется " +
            "c использованием моков для LOGIN действия")
    public void testLogoutAfterLoginSuccessWithMockedLogin() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        Response response = sendPostLogoutWithoutStatusCheck(ENDPOINT, token);

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
    @Features(value = {@Feature(value = "LOGOUT"), @Feature(value = "US-CASE")})
    @Story(value = "Проверка успешного LOGOUT после LOGIN и одного ACTION действия пользователя " +
            "c использованием моков для LOGIN,ACTION действий")
    public void testLogoutAfterLoginAndActionSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));
        sendPostActionCheckStatus200(ENDPOINT, token);

        Response response = sendPostLogoutWithoutStatusCheck(ENDPOINT, token);

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
    @Features(value = {@Feature(value = "LOGOUT"), @Feature(value = "US-CASE")})
    @Story(value = "Проверка успешного LOGOUT после LOGIN и нескольких(2) ACTION действий пользователя " +
            "c использованием моков для LOGIN,ACTION действий")
    public void testLogoutAfterLoginAndActionsSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));
        for (int i = 0; i < 2; i++)
            sendPostActionCheckStatus200(ENDPOINT, token);

        Response response = sendPostLogoutWithoutStatusCheck(ENDPOINT, token);

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
    @Feature(value = "LOGOUT")
    @Story(value = "POST запрос для LOGOUT (после LOGIN с сохранением token) " +
            "выполняется при отсутствии заголовка 'Content-Type'")
    public void testLogoutMissedContentTypeSuccessWithMockedLogin() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.LOGOUT, Status.SUCCESS.getCode(), false);

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
    @Feature(value = "LOGOUT")
    @Story(value = "POST запрос для LOGOUT (после LOGIN с сохранением token) " +
            "выполняется при отсутствии заголовка 'Accept'")
    public void testLogoutMissedAcceptSuccessWithMockedLogin() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Content-Type", "application/x-www-form-urlencoded"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.LOGOUT, Status.SUCCESS.getCode(), false);

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
    @Feature(value = "LOGOUT")
    @Story(value = "Проверка среднего времени ответа у 10 последовательных успешных POST запросов для действия LOGOUT " +
            "на соответствие условному среднему отличному результату (меньше 200мс) " +
            "c использованием моков для LOGIN действия")
    public void testLogoutAverageResponseTimeSuccessWithMockedLogin() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        long sumTime = 0;
        for (int i = 0; i < REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS; i++) {
            String token = TokenGenerator.getHexadecimalToken();
            sendPostLoginCheckStatus200(ENDPOINT, token);
            sumTime += sendPostLogoutCheckStatus200(ENDPOINT, token)
                    .getTime();
        }
        double actualAverageTime = (double) sumTime / REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS;

        assertTrue(EXPECTED_AVERAGE_TIME > actualAverageTime,
                "Актуальное среднее время ответа: " + actualAverageTime + " выше ожидаемого: " + EXPECTED_AVERAGE_TIME);
    }
}