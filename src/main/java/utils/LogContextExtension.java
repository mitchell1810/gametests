package utils;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.MDC;

public class LogContextExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        // Достаем имя метода перед началом каждого теста
        String testName = context.getRequiredTestMethod().getName();
        // getRequiredTestMethod() гарантирует, что мы внутри теста.
        MDC.put("testName", testName);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        // Очищаем после завершения каждого теста
        MDC.remove("testName");
    }
}