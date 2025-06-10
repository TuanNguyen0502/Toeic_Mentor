package intern.nhhtuan.toeic_mentor.service.interfaces;

import reactor.core.publisher.Flux;

import java.io.InputStream;

public interface IChatService {
    Flux<String> getChatResponse(String message, String conversationId);

    String createTest(InputStream imageInputStream, String contentType, String imageUrl);

    String definePart(InputStream imageInputStream, String contentType);
}
