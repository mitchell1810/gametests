package utils;

import lombok.extern.slf4j.Slf4j;
import modelsDTO.ResponseBodyDTO;

@Slf4j
public abstract class ResponseBodyObtainer extends DataObtainer {

    public static ResponseBodyDTO convertJsonDataToResponseBodyDTO(String jsonDataFromAPI) {
        return convertJsonDataToDTO(jsonDataFromAPI, ResponseBodyDTO.class);
    }
}
