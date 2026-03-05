package tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
public class BaseWireMockTest extends BaseRestAssuredTest {

    protected static final long EXPECTED_AVERAGE_TIME = 200L;
    protected static final int REQUEST_COUNT_FOR_AVERAGE_TIME_TESTS = 10;

    protected static final String AUTH_MOCK_URL = appConfigLoader.getProperties().getProperty("mock.auth.url");
    protected static final String DO_ACTION_MOCK_URL = appConfigLoader.getProperties().getProperty("mock.do.action.url");

    private static final int PORT = Integer.parseInt(appConfigLoader.getProperties().getProperty("wiremock.port"));
    private static final String HOST = appConfigLoader.getProperties().getProperty("wiremock.host");
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void flagWireMock() {
        wiremockIsRunning = true;
    }

    @BeforeEach
    public void startWireMock() {
        wireMockServer = new WireMockServer(options().port(PORT));
        wireMockServer.start();
        configureFor(HOST, PORT);
    }

    @AfterEach
    public void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @AfterAll
    public static void unFlagWireMock() {
        wiremockIsRunning = false;
    }

    protected void setupMocks(String mockUrl, int expectedStatusCode){
        log.info("Устанавливаю моки для {} c возвратом Status Code {}", mockUrl, expectedStatusCode);
        stubFor(post(mockUrl).willReturn(aResponse().withStatus(expectedStatusCode)));
    }

}
