package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.ReportRequest;

public interface IReportService {
    boolean saveReport(String email, ReportRequest request);
}
