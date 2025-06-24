package intern.nhhtuan.toeic_mentor.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.List;

public interface IChatService {
    Flux<String> getChatResponse(String message, String conversationId, InputStream imageInputStream, String contentType);

    Flux<String> getChatResponse(String message, String conversationId);

    List<QuestionDTO> createTest(InputStream imageInputStream, String contentType, List<String> imageUrls, String part7PreviousContent);

    String definePart(InputStream imageInputStream, String contentType);

    String identifyToeicTest(InputStream imageInputStream, String contentType);

    List<String> getChatHistory(String conversationId);

    List<String> getConversationIdsByEmail(String email);

    String buildTestAnalysisPrompt(List<AnswerRequest> answerRequests) throws JsonProcessingException;

    Flux<String> analyzeTestResult(List<AnswerRequest> answerRequests, String conversationId);

    String mergeJsonResponses(List<String> jsonResponses);

    void deleteByConversationId(String conversationId);

    String generateConversationId(String message, String email);

    boolean renameConversation(String oldConversationId, String newConversationId);
}
