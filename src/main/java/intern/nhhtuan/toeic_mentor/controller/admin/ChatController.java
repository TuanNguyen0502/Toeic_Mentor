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
        StringBuilder part7PreviousContent = new StringBuilder(); // Store previous content for PART_7
        int maxAttempts = 3; // Maximum attempts to process each image

        for (MultipartFile imageFile : imageFiles) {
            // Check if the uploaded file is empty
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image file cannot be empty"));
            }
            // Check if the uploaded file is a PNG or JPEG image
            if (!Objects.equals(imageFile.getContentType(), MediaType.IMAGE_PNG_VALUE) &&
                    !Objects.equals(imageFile.getContentType(), MediaType.IMAGE_JPEG_VALUE)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PNG and JPEG files are supported"));
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                // Check if the uploaded file is TOEIC test
                int identifyToeicAttempts = 0;
                String identifyResult;
                do {
                    identifyResult = chatService.identifyToeicTest(imageFile.getInputStream(), imageFile.getContentType());
                    if (identifyResult.equals("TOEIC_TRUE")) {
                        break; // Exit the loop if TOEIC test is identified
                    } else if (identifyResult.equals("TOEIC_FALSE")) {
                        return ResponseEntity.badRequest().body(Map.of("error", "One or more images are not TOEIC test images"));
                    }
                    identifyToeicAttempts++; // Increment the attempt count
                } while (identifyToeicAttempts < maxAttempts);
                // If the maximum attempts are reached and the result is still not identified
                if (!identifyResult.equals("TOEIC_TRUE")) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Failed to identify TOEIC test after maximum attempts"));
                }

                // Call service to define image's part
                int definePartAttempts = 0; // Counter for attempts to define part
                String part;
                do {
                    part = chatService.definePart(inputStream, imageFile.getContentType());
                    // If the part is 7 or NO_QUESTION then save the image and get the URL
                    if (part.contains("PART_7")) {
                        imageUrls.add(imageUtil.saveImage(imageFile));
                    }
                    // If the part is NO_QUESTION, remove the image from the list
                    // so it won't be processed further
                    if (part.contains("PART_7_NO_QUESTION")) {
                        indexOfPart7NoQuestion.add(imageFiles.indexOf(imageFile));
                        // Store the previous content for PART_7_NO_QUESTION
                        part7PreviousContent.append(part.replace("PART_7_NO_QUESTION_", ""));
                    }
                    // If the part is PART_5, PART_6, or PART_7, break the loop
                    if (part.contains("PART_5") || part.contains("PART_6") || part.contains("PART_7")) {
                        break;
                    }

                    definePartAttempts++; // Increment the attempt count
                } while (definePartAttempts < maxAttempts);
                // If the maximum attempts are reached and the part is still not defined
                if (!part.contains("PART_5") && !part.contains("PART_6") && !part.contains("PART_7")) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Failed to define part after maximum attempts"));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing image file"));
            }
        }
        if (indexOfPart7NoQuestion.size() == imageFiles.size()) {
            // If all images are PART_7_NO_QUESTION, return an error
            return ResponseEntity.badRequest().body(Map.of("error", "All images do not have any questions to process"));
        }

        // Remove images that are only PART_7_NO_QUESTION
        // from the list of image files to avoid processing them
        if (!indexOfPart7NoQuestion.isEmpty()) {
            for (int index : indexOfPart7NoQuestion) {
                imageFiles.remove(index); // Remove images that are only PART_7_NO_QUESTION
            }
        }

        List<QuestionRequest> questionRequests = new ArrayList<>(); // Store question requests
        for (MultipartFile imageFile : imageFiles) {
            try (InputStream inputStream = imageFile.getInputStream()) {
                // Create a test with the image input stream and content type
                questionRequests.addAll(chatService.createTest(inputStream, imageFile.getContentType(), imageUrls, part7PreviousContent.toString()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing image file"));
            }
        }

        return ResponseEntity.ok(questionRequests);
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestBody List<QuestionRequest> questions) {
        questionService.saveQuestionsFromDTO(questions);
        return ResponseEntity.ok(Map.of("message", "Saved successfully"));
    }
}
