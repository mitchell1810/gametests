package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import utils.AppConfigLoader;
//import utils.in_test.RestAssuredConfig;

public class BaseRestAssuredTest {
    protected static AppConfigLoader appConfigLoader = new AppConfigLoader();
    protected static String ENDPOINT = appConfigLoader.getProperties().getProperty("app.endpoint");

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = appConfigLoader.getProperties().getProperty("app.base.uri");
        RestAssured.port = Integer.parseInt(appConfigLoader.getProperties().getProperty("app.port"));
//        RestAssuredConfig.setup();
    }

}
