package tests.actions.action;

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

import static helpers.RestHelper.sendPostActionWithoutStatusCheck;
import static helpers.RestHelper.sendPostLoginCheckStatus200;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ResponseBodyObtainer.convertJsonDataToResponseBodyDTO;

@Slf4j
@ExtendWith(utils.LogContextExtension.class)
public class TestActionWithImitationOfLogin extends BaseWireMockTest {

    @Test
    @Epic(value = "Проверка действия пользователя")
    @Feature(value = "ACTION")
    @Story(value = "Проверка успешного ACTION с сохранённым token, соответствующим выражению из ошибки приложения: \"^[0-9A-F]{32}$\" " +
            "после успешного логина")
    public void testActionWithStoredHexadecimalTokenSuccessWithMockedLogin() {

        setupMocks(AUTH_MOCK_URL, Status.SUCCESS.getCode());

        String token = TokenGenerator.getHexadecimalToken();

        sendPostLoginCheckStatus200(ENDPOINT, token);

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