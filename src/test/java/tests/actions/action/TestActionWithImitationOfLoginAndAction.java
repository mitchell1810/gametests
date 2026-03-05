package tests.actions.action;

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
import static helpers.RestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@ExtendWith(utils.LogContextExtension.class)
public class TestActionWithImitationOfLoginAndAction extends BaseWireMockTest {

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (после LOGIN с сохранением token) " +
            "выполняется при отсутствии заголовка 'Content-Type'")
    public void testActionMissedContentTypeSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));
        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.ACTION, Status.SUCCESS.getCode(), false);

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (после LOGIN с сохранением token) " +
            "выполняется при отсутствии заголовка 'Accept'")
    public void testActionMissedAcceptSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        Map<String, String> headersMap = new HashMap<>(Map.ofEntries(
                Map.entry("Content-Type", "application/x-www-form-urlencoded"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));

        Response response = sendPostWithDynamicHeadersAndParams(
                ENDPOINT, headersMap, token, Actions.ACTION, Status.SUCCESS.getCode(), false);

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для ACTION (после LOGIN с сохранением token) выполняется")
    public void testActionAfterLoginSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        Response response = sendPostActionWithoutStatusCheck(ENDPOINT, token);

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
    @Feature(value = "ACTION")
    @Story(value = "POST запрос для нескольких(2) ACTION (после LOGIN с сохранением token) выполняется")
    public void testActionsAfterLoginSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        for (int i = 0; i < 2; i++) {
            Response response = sendPostActionWithoutStatusCheck(ENDPOINT, token);

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
    }

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "ACTION")
    @Story(value = "Проверка среднего времени ответа у 10 последовательных успешных POST запросов для действия ACTION " +
            "на соответствие условному среднему отличному результату (меньше 200мс) " +
            "c использованием моков для LOGIN, ACTION действий")
    public void testActionAverageResponseTimeSuccessWithMockedFully() {

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", AUTH_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(AUTH_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

        log.info("Устанавливаю моки для {} c возвратом Status Code {}", DO_ACTION_MOCK_URL, Status.SUCCESS.getCode());
        stubFor(post(DO_ACTION_MOCK_URL).willReturn(aResponse().withStatus(Status.SUCCESS.getCode())));

        long sumTime = 0;
        for (int i = 0; i < REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS; i++) {
            sumTime += sendPostActionCheckStatus200(ENDPOINT, token)
                    .getTime();
        }
        double actualAverageTime = (double) sumTime / REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS;

        assertTrue(EXPECTED_AVERAGE_TIME > actualAverageTime,
                "Actual average response time " + actualAverageTime + " higher than expected " + EXPECTED_AVERAGE_TIME);
    }
}