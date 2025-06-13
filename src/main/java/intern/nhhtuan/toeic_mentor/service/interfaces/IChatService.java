package intern.nhhtuan.toeic_mentor.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.List;

public interface IChatService {
    Flux<String> getChatResponse(String message, String conversationId);

    String createTest(InputStream imageInputStream, String contentType, List<String> imageUrls, String part7PreviousContent);

    String definePart(InputStream imageInputStream, String contentType);

    List<String> getChatHistory(String conversationId);

    String buildToeicAnalysisPrompt(List<QuestionResponse> responses) throws JsonProcessingException;

    String mergeJsonResponses(List<String> jsonResponses);
}
