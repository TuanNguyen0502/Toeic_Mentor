package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_feedbacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback", nullable = false)
    private EChatbotFeedback feedback;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatbot_response_id")
    private ChatMemory chatMemory;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
