package intern.nhhtuan.toeic_mentor.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.request.QuestionRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@RestController("AdminChatController")
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class ChatController {
    private final IChatService chatService;
    private final IQuestionService questionService;
    private final ImageUtil imageUtil;

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") List<MultipartFile> imageFiles) {
        // Check if the list of image files is empty
        if (imageFiles.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Image file cannot be empty"));
        }

        List<String> imageUrls = new ArrayList<>(); // Store URLs of uploaded images

        List<Integer> indexOfPart7NoQuestion = new ArrayList<>(); // Store indices of images that are PART_7_NO_QUESTION
        for (MultipartFile imageFile : imageFiles) {
            // Check if the uploaded file is empty
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image file cannot be empty"));
            }
            // Check if the uploaded file is a PNG or JPEG image
            if (!Objects.equals(imageFile.getContentType(), MediaType.IMAGE_PNG_VALUE) ||
                    !Objects.equals(imageFile.getContentType(), MediaType.IMAGE_JPEG_VALUE)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PNG files are supported"));
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                // Call service to define image's part
                String part = chatService.definePart(inputStream, imageFile.getContentType());
                // If the part is 7 or NO_QUESTION then save the image and get the URL
                if (part.contains("PART_7")) {
                    imageUrls.add(imageUtil.saveImage(imageFile));
                }
                // If the part is NO_QUESTION, remove the image from the list
                // so it won't be processed further
                if (part.contains("PART_7_NO_QUESTION")) {
                    indexOfPart7NoQuestion.add(imageFiles.indexOf(imageFile));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing image file"));
            }
        }

        // Remove images that are only PART_7_NO_QUESTION
        // from the list of image files to avoid processing them
        if (!indexOfPart7NoQuestion.isEmpty()) {
            for (int index : indexOfPart7NoQuestion) {
                imageFiles.remove(index); // Remove images that are only PART_7_NO_QUESTION
            }
        }

        List<String> chatbotResponses = new ArrayList<>(); // Store chatbot responses of each image
        for (MultipartFile imageFile : imageFiles) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                // Create a test with the image input stream and content type
                String chatbotResponse = chatService.createTest(inputStream, imageFile.getContentType(), imageUrls);
                chatbotResponses.add(chatbotResponse);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing image file"));
            }
        }

        // Combine all responses into a single string
        String result = chatService.mergeJsonResponses(chatbotResponses);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Parse the result string to a List of Map
            List<Map<String, Object>> resultList = objectMapper.readValue(result, new TypeReference<List<Map<String, Object>>>() {});
            return ResponseEntity.ok(resultList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error parsing result"));
        }
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestBody List<QuestionRequest> questions) {
        questionService.saveQuestionsFromDTO(questions);
        return ResponseEntity.ok(Map.of("message", "Saved successfully"));
    }
}
