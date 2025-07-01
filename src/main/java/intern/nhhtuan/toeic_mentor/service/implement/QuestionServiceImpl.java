package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.dto.QuestionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.dto.response.SectionQuestionResponse;
import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.ESectionStatus;
import intern.nhhtuan.toeic_mentor.repository.*;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionOptionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final IQuestionOptionService questionOptionService;
    private final IQuestionTagService questionTagService;
    private final QuestionImageRepository imageRepository;
    private final PartRepository partRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    @Override
    public void saveQuestionsFromDTO(List<QuestionDTO> dtoList, Long sectionId) {
        for (QuestionDTO dto : dtoList) {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new IllegalArgumentException("Section not found with ID: " + sectionId));
            section.setStatus(ESectionStatus.PENDING_REVIEW); // Đặt trạng thái của section là PENDING_REVIEW
            sectionRepository.save(section); // Lưu section với trạng thái mới

            Question question = new Question();
            question.setQuestionNumber(dto.getQuestionNumber());
            question.setQuestionText(dto.getQuestionText());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            question.setPassage(dto.getPassage());
            question.setPart(partRepository.findByName(getPartName(dto.getPart())));
            question.setStatus(EQuestionStatus.IN_SECTION);
            question.setExplanation(dto.getExplanation());
            question.setSection(section);
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
            questionOptionService.saveOptions(dto.getOptions(), question);

            // Lưu tags
            questionTagService.saveTags(dto.getTags(), question);
        }
    }

    @Override
    public boolean update(QuestionUpdateDTO questionUpdateDTO) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionUpdateDTO.getId());
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setPart(partRepository.findByName(getPartName(questionUpdateDTO.getPart())));
            question.setQuestionNumber(questionUpdateDTO.getNumber());
            question.setPassage(questionUpdateDTO.getPassage());
            question.setQuestionText(questionUpdateDTO.getContent());
            question.setCorrectAnswer(questionUpdateDTO.getCorrectAnswer());
            question.setStatus(questionUpdateDTO.getStatus());

            // Save question with updated fields
            questionRepository.save(question);

            // Save new options
            questionOptionService.saveOptions(questionUpdateDTO.getOptions(), question);

            // Save new tags
            questionTagService.saveTags(questionUpdateDTO.getTags(), question);

            return true;
        }
        return false;
    }

    @Override
    public QuestionUpdateDTO getQuestionUpdateById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID: " + questionId));

        List<String> imageUrls = question.getPassageImageUrls().stream()
                .map(QuestionImage::getImage)
                .collect(Collectors.toList());

        Map<String, String> options = question.getOptions().stream()
                .collect(Collectors.toMap(QuestionOption::getKey, QuestionOption::getValue));

        List<String> tags = question.getTags().stream()
                .map(QuestionTag::getTag)
                .collect(Collectors.toList());

        return QuestionUpdateDTO.builder()
                .id(question.getId())
                .part(Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")))
                .number(question.getQuestionNumber())
                .imageUrls(imageUrls)
                .passage(question.getPassage())
                .content(question.getQuestionText())
                .options(options)
                .correctAnswer(question.getCorrectAnswer())
                .tags(tags)
                .status(question.getStatus())
                .build();
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
            // If topics are specified, fetch questions that match both part and topics
            List<QuestionTag> tags = new ArrayList<>();
            for (String tag : request.getTopic()) {
                tags.addAll(questionTagService.findByTag(tag));
            }
            // If no topics are specified, fetch all questions for the part
            if (request.getTopic().size() == 0 || tags.isEmpty()) {
                questions = questionRepository.findDistinctByPart_NameAndStatus(partName, EQuestionStatus.APPROVED);
            } else {
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
