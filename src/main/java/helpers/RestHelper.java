package helpers;

import enums.Actions;
import enums.Status;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Slf4j
public class RestHelper {

    public static Map<String, String> getCorrectHeaders() {
        return new HashMap<>(Map.ofEntries(
                Map.entry("Accept", "application/json"),
                Map.entry("Content-Type", "application/x-www-form-urlencoded"),
                Map.entry("X-Api-Key", "qazWSXedc")
        ));
    }

    public static Response sendPostLoginWithoutStatusCheck(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.LOGIN,
                Status.SUCCESS.getCode(),
                false);
    }

    public static Response sendPostLoginCheckStatus200(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.LOGIN,
                Status.SUCCESS.getCode(),
                true);
    }

    public static Response sendPostActionWithoutStatusCheck(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.ACTION,
                Status.SUCCESS.getCode(),
                false);
    }

    public static Response sendPostActionCheckStatus200(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.ACTION,
                Status.SUCCESS.getCode(),
                true);
    }

    public static Response sendPostLogoutWithoutStatusCheck(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.LOGOUT,
                Status.SUCCESS.getCode(),
                false);
    }

    public static Response sendPostLogoutCheckStatus200(String entityUrl, String token) {
        return sendPostWithDynamicHeadersAndParams(
                entityUrl,
                getCorrectHeaders(),
                token,
                Actions.LOGOUT,
                Status.SUCCESS.getCode(),
                true);
    }

    /**
     * Отправляет POST запрос с добавлением произвольных заголовков и параметров.
     * Возможность вариативной проверки статуса зависит от флага checkStatus.
     *
     * @param entityUrl      URL конечной точки (endpoint)
     * @param headers        Мап заголовков (если пустое - ничего не добавляется)
     * @param token          Ожидаемый токен (если null - ничего не добавляется)
     * @param action         Ожидаемое действие (если null - ничего не добавляется)
     * @param expectedStatus Ожидаемый статус-код ответа
     * @param checkStatus    Флаг, разрешающий/запрещающий проверку статуса
     * @return Полный объект Response с результатами запроса
     */
    public static Response sendPostWithDynamicHeadersAndParams(
            String entityUrl,
            Map<String, String> headers,
            String token,
            Actions action,
            int expectedStatus,
            boolean checkStatus
    ) {
        var parameters = addParameters(token, action);

        log.info("Посылаю POST запрос на эндпоинт '{}' " +
                        "c параметром Action = '{}' " +
                        "c параметром token = '{}' " +
                        "с заголовками: '{}'",
                entityUrl,
                (action == null) ? "null" : action.name(),
                (token == null) ? "null" : token,
                headers
        );

        Response response = given()
                .log().all()
                .headers(headers)
                .params(parameters)
                .when()
                .post(entityUrl);

        if (checkStatus) {
            log.info("Проверяю Status Code на соответствие '{}' коду", expectedStatus);
            response.then().assertThat().statusCode(expectedStatus);
        }

        return response;
    }

    private static Map<String, Object> addParameters(String token, Actions action) {
        var parameters = new HashMap<String, Object>();
        if (token != null)
            parameters.put("token", token);
        if (action != null)
            parameters.put("action", action.name());
        return parameters;
    }
}
