package intern.nhhtuan.toeic_mentor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class QuestionImageUpdateDTO {
    private Long questionId;

    private List<String> existingImagePaths;

    private MultipartFile[] newImages;
}
