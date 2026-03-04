package modelsDTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBodyDTO {
    @NonNull
    private String result;
    private String message;
}
