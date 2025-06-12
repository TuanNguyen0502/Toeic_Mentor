package intern.nhhtuan.toeic_mentor.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.request.QuestionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonToDtoConverter {

    private final ObjectMapper objectMapper;

    public List<QuestionRequest> parseJsonString(String jsonString) {
        try {
            // Xử lý prefix "json"
            String cleanJson = jsonString.trim();
            if (cleanJson.startsWith("json")) {
                cleanJson = cleanJson.substring(cleanJson.indexOf("[")); // Bắt đầu từ '['
            }
            return objectMapper.readValue(cleanJson, new TypeReference<List<QuestionRequest>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi parse JSON: " + e.getMessage(), e);
        }
    }
}
