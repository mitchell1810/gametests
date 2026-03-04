package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class DataObtainer {

    protected static <T> T convertJsonDataToDTO(String jsonDataFromAPI, Class<T> entityClass) {
        try {
            return new ObjectMapper().readValue(jsonDataFromAPI, entityClass);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
