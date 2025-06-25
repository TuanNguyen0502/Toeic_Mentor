package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionImageUpdateDTO;
import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionImage;
import intern.nhhtuan.toeic_mentor.repository.QuestionImageRepository;
import intern.nhhtuan.toeic_mentor.repository.QuestionRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionImageService;
import intern.nhhtuan.toeic_mentor.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionImageServiceImpl implements IQuestionImageService {
    private final QuestionImageRepository questionImageRepository;
    private final QuestionRepository questionRepository;
    private final ImageUtil imageUtil;

    @Override
    public QuestionImageUpdateDTO getQuestionImageUpdateByQuestionId(Long questionId) {
        List<QuestionImage> questionImages = questionImageRepository.findByQuestion_Id(questionId);
        return QuestionImageUpdateDTO.builder()
                .questionId(questionId)
                .existingImagePaths(questionImages.stream()
                        .map(QuestionImage::getImage)
                        .toList())
                .build();
    }

    @Override
    public boolean updateQuestionImage(QuestionImageUpdateDTO questionImageUpdateDTO) {
        Question question = questionRepository.findById(questionImageUpdateDTO.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Câu hỏi không tồn tại"));

        if (questionImageUpdateDTO.getExistingImagePaths() != null) {
            Set<String> keep = new HashSet<>(questionImageUpdateDTO.getExistingImagePaths());
            List<QuestionImage> allImages = questionImageRepository.findByQuestion_Id(questionImageUpdateDTO.getQuestionId());
            List<QuestionImage> toDelete = allImages.stream()
                    .filter(img -> !keep.contains(img.getImage()))
                    .toList();
            questionImageRepository.deleteAll(toDelete);
            for (QuestionImage img : toDelete) {
                try {
                    imageUtil.deleteImage(img.getImage());
                } catch (IOException e) {
                    throw new RuntimeException("Không xoá được ảnh " + img.getImage(), e);
                }
            }
        }

        MultipartFile[] files = questionImageUpdateDTO.getNewImages();
        if (files != null) {
            for (MultipartFile f : files) {
                if (f != null && !f.isEmpty()) {
                    String original = f.getOriginalFilename();
                    if (!imageUtil.isValidSuffixImage(original)) {
                        throw new IllegalArgumentException("Định dạng không hợp lệ: " + original);
                    }
                    try {
                        String url = imageUtil.saveImage(f);
                        QuestionImage newImg = QuestionImage.builder()
                                .image(url)
                                .question(question)
                                .build();
                        questionImageRepository.save(newImg);
                    } catch (IOException e) {
                        throw new RuntimeException("Không lưu được ảnh " + original, e);
                    }
                }
            }
        }

        return true;
    }
}
