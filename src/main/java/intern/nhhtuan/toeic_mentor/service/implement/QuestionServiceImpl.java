package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionAnswerStats;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableAsync
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final IQuestionOptionService questionOptionService;
    private final QuestionImageRepository imageRepository;
    private final PartRepository partRepository;
    private final SectionRepository sectionRepository;
    private final AnswerRepository answerRepository;

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
            question.setAnswerExplanation(dto.getAnswerExplanation());
            question.setTags(dto.getTags());
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
            question.setAnswerExplanation(questionUpdateDTO.getAnswerExplanation());
            question.setTags(questionUpdateDTO.getTags());

            // Save question with updated fields
            questionRepository.save(question);

            // Save new options
            questionOptionService.saveOptions(questionUpdateDTO.getOptions(), question);

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

        return QuestionUpdateDTO.builder()
                .id(question.getId())
                .part(Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")))
                .number(question.getQuestionNumber())
                .imageUrls(imageUrls)
                .passage(question.getPassage())
                .content(question.getQuestionText())
                .options(options)
                .correctAnswer(question.getCorrectAnswer())
                .tags(question.getTags())
                .status(question.getStatus())
                .answerExplanation(question.getAnswerExplanation())
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
                        .tags(String.join(", ", question.getTags()))
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
        // Fetch questions based on part and topic
        List<Question> questions = new ArrayList<>();

        // Validate request
        for (Integer part : request.getPart()) {
            if (part < 1 || part > 7) {
                throw new IllegalArgumentException("Part must be between 1 and 7");
            }
            EPart partName = getPartName(part);

            // If no topics are specified, fetch all questions for the part
            if (request.getTopic().size() == 0) {
                questions = questionRepository.findDistinctByPart_NameAndStatus(partName, EQuestionStatus.APPROVED);
            } else {
                questions = questionRepository.findDistinctByPart_NameAndTagsAndStatus(partName, request.getTopic(), EQuestionStatus.APPROVED);
                if (questions.size() < request.getQuestion_count()) {
                    List<Question> temp = questionRepository.findDistinctByPart_NameAndStatus(
                            partName,
                            EQuestionStatus.APPROVED,
                            Limit.of(request.getQuestion_count() - questions.size())
                    );
                    questions.addAll(temp);
                }
            }
        }

        // Shuffle and limit
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.stream()
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

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void calculateDifficulty() {
        try {
            System.out.println("Chạy lúc 12h đêm: " + System.currentTimeMillis() / 1000);

            // Lấy tất cả Question đưa vào Map để tra nhanh
            List<Question> allQuestions = questionRepository.findAll();
            Map<Long, Question> questionMap = allQuestions.stream()
                    .collect(Collectors.toMap(Question::getId, Function.identity()));

            List<QuestionAnswerStats> statsList = answerRepository.getAnswerStatistics();

            List<Question> toUpdate = new ArrayList<>();

            for (QuestionAnswerStats stats : statsList) {
                double wrongRatio = stats.getTotalCount() > 0
                        ? (double) stats.getWrongCount() / stats.getTotalCount()
                        : 0;

                int difficulty;
                if (wrongRatio <= 0.2) {
                    difficulty = 1;
                } else if (wrongRatio <= 0.4) {
                    difficulty = 2;
                } else if (wrongRatio <= 0.6) {
                    difficulty = 3;
                } else if (wrongRatio <= 0.8) {
                    difficulty = 4;
                } else {
                    difficulty = 5;
                }

                Question q = questionMap.get(stats.getQuestionId());
                if (q.getDifficulty() == null || q.getDifficulty() != difficulty) {
                    q.setDifficulty(difficulty);
                    toUpdate.add(q);
                }
            }

            // Lưu lại toàn bộ
            questionRepository.saveAll(toUpdate);

            System.out.println("Updated " + toUpdate.size() + " questions");
        } catch (Exception ex) {
            // log lỗi
            ex.printStackTrace();
        }
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

        List<String> passageImageUrls = question.getPassageImageUrls().stream()
                .map(QuestionImage::getImage)
                .toList();

        return QuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .correctAnswer(question.getCorrectAnswer())
                .answerExplanation(question.getAnswerExplanation())
                .difficulty(question.getDifficulty())
                .userAnswer(null)
                .passage(question.getPassage())
                .passageImageUrls(passageImageUrls)
                .part(Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")))
                .options(options)
                .tags(question.getTags())
                .build();
    }
}
