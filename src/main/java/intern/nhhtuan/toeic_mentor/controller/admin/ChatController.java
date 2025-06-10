package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("AdminChatController")
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class ChatController {
    private final IChatService chatService;
    private final IQuestionService questionService;
    private final ImageUtil imageUtil;

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Image file cannot be empty"));
        }

        // Check if the uploaded file is a PNG
        if (!imageFile.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only PNG files are supported"));
        }

        String isPart7;
        String imageUrl = "null";
        try (InputStream inputStream = imageFile.getInputStream()) {
            isPart7 = chatService.definePart(inputStream, imageFile.getContentType());
            if (isPart7.toLowerCase().contains("true")) {
                imageUrl = imageUtil.saveImage(imageFile);
            }
        } catch (Exception e) {
            isPart7 = "Error uploading image";
        }
        String result;
        try (InputStream inputStream = imageFile.getInputStream()) {
            result = chatService.createTest(inputStream, imageFile.getContentType(), imageUrl);
        } catch (Exception e) {
            result = "Error uploading image";
        }

        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestBody List<QuestionDTO> questions) {
        questionService.saveQuestionsFromDTO(questions);
        return ResponseEntity.ok(Map.of("message", "Saved successfully"));
    }
}
