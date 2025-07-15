package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;

import java.io.ByteArrayOutputStream;

public interface IPdfService {
    ByteArrayOutputStream generateTestResultPdf(TestResultResponse testResult);
} 