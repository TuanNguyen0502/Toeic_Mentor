package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements IPdfService {
    
    private final TemplateEngine templateEngine;
    
    @Override
    public ByteArrayOutputStream generateTestResultPdf(TestResultResponse testResult) {
        try {
            log.info("Generating PDF for test result with {} questions", testResult.getAnswerResponses().size());
            
            // Create Thymeleaf context and add data
            Context context = new Context();
            context.setVariable("score", testResult.getScore());
            context.setVariable("total", testResult.getAnswerResponses().size());
            context.setVariable("correctPercent", testResult.getCorrectPercent());
            context.setVariable("performance", testResult.getPerformance());
            context.setVariable("recommendations", testResult.getRecommendations());
            context.setVariable("answers", testResult.getAnswerResponses());
            context.setVariable("referenceUrls", testResult.getReferenceUrls());
            
            // Process the template
            String htmlContent = templateEngine.process("test-result-pdf", context);
            log.debug("HTML content generated successfully");
            
            // Generate PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            log.info("PDF generated successfully, size: {} bytes", outputStream.size());
            return outputStream;
            
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
} 