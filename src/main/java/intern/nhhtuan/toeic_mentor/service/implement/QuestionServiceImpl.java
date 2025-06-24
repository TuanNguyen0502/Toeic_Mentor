package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.dto.response.SectionQuestionResponse;
import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.repository.*;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuestionTagRepository tagRepository;
    private final QuestionImageRepository imageRepository;
    private final PartRepository partRepository;

    @Transactional
    @Override
    public void saveQuestionsFromDTO(List<QuestionDTO> dtoList) {
        for (QuestionDTO dto : dtoList) {
            Question question = new Question();
            question.setQuestionNumber(dto.getQuestionNumber());
            question.setQuestionText(dto.getQuestionText());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            question.setPassage(dto.getPassage());
            question.setPart(partRepository.findByName(getPartName(dto.getPart())));

            // Lưu trước để có ID cho liên kết
            questionRepository.save(question);

            // Lưu images
            if (dto.getPassageImageUrls() != null) {
                for (String image : dto.getPassageImageUrls()) {
                    QuestionImage questionImage = new QuestionImage(image, question);
                    imageRepository.save(questionImage);
                }
            }

            // Lưu options
            for (var entry : dto.getOptions().entrySet()) {
                QuestionOption option = new QuestionOption(entry.getKey(), entry.getValue(), question);
                optionRepository.save(option);
            }

            // Lưu tags
            for (String tagStr : dto.getTags()) {
                QuestionTag tag = new QuestionTag(tagStr, question);
                tagRepository.save(tag);
            }
        }
    }

    @Override
    public List<SectionQuestionResponse> getQuestionResponseBySectionId(Long sectionId) {
        List<Question> questions = questionRepository.findBySection_Id(sectionId);
        return questions.stream()
                .map(question -> SectionQuestionResponse.builder()
                        .id(question.getId())
                        .part(Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")))
                        .status(question.getStatus().name())
                        .text(question.getQuestionText())
                        .correctAnswer(question.getCorrectAnswer() + ". " + question.getOptions().stream()
                                .filter(opt -> opt.getKey().equals(question.getCorrectAnswer()))
                                .map(QuestionOption::getValue)
                                .findFirst()
                                .orElse(""))
                        .tags(question.getTags().stream()
                                .map(QuestionTag::getTag)
                                .collect(Collectors.joining(", ")))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateQuestionStatus(Long questionId, EQuestionStatus status) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setStatus(status);
            questionRepository.save(question);
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionResponse> generateTest(TestRequest request) {
        List<Question> matchedQuestions = new ArrayList<>();

        // Validate request
        for (Integer part : request.getPart()) {
            if (part < 1 || part > 7) {
                throw new IllegalArgumentException("Part must be between 1 and 7");
            }
            EPart partName = getPartName(part);
            // Fetch questions based on part and topic
            List<Question> questions;
            // If no topics are specified, fetch all questions for the part
            if (request.getTopic().size() == 0) {
                questions = questionRepository.findDistinctByPart_NameAndStatus(partName, EQuestionStatus.APPROVED);
            } else {
                // If topics are specified, fetch questions that match both part and topics
                List<QuestionTag> tags = new ArrayList<>();
                for (String tag : request.getTopic()) {
                    tags.addAll(tagRepository.findByTag(tag));
                }
                questions = questionRepository.findDistinctByPart_NameAndTagsAndStatus(partName, tags, EQuestionStatus.APPROVED);
            }
            // Add the fetched questions to the matched list
            matchedQuestions.addAll(questions);
        }

        // Shuffle and limit
        Collections.shuffle(matchedQuestions);
        List<Question> selectedQuestions = matchedQuestions.stream()
                .limit(request.getQuestion_count())
                .toList();

        // Convert to DTOs
        return selectedQuestions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int countByPart_Id(Long partId) {
        return questionRepository.countByPart_Id(partId);
    }

    @Override
    public int getTotalQuestions() {
        return questionRepository.findAll().size();
    }

    @Override
    public Optional<Question> findById(Long aLong) {
        return questionRepository.findById(aLong);
    }

    private EPart getPartName(Integer part) {
        return Arrays.stream(EPart.values())
                .filter(p -> p.name().contains(part.toString()))
                .findFirst()
                .orElse(null);
    }

    private QuestionResponse convertToDTO(Question question) {
        List<QuestionResponse.OptionResponse> options = question.getOptions().stream()
                .map(opt -> new QuestionResponse.OptionResponse(opt.getKey(), opt.getValue()))
                .collect(Collectors.toList());

        List<String> tags = question.getTags().stream()
                .map(QuestionTag::getTag)
                .collect(Collectors.toList());

        List<String> passageImageUrls = question.getPassageImageUrls().stream()
                .map(QuestionImage::getImage)
                .toList();

        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getCorrectAnswer(),
                null,
                question.getPassage(),
                passageImageUrls,
                Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")),
                options,
                tags
        );
    }
}
