package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.entity.Report;

public interface INotificationService {
    void createReportNotifications(Report report);
    void createResponseUserNotifications(ReportDetailDTO reportDetailDTO);
}
