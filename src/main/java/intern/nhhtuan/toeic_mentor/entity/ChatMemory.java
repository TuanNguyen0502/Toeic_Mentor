package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EChatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "SPRING_AI_CHAT_MEMORY")
@Getter
@Setter
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
