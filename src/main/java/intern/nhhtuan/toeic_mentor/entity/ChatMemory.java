package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "SPRING_AI_CHAT_MEMORY")
public class ChatMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private String conversationId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EChatType type;

    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp timestamp;
}
