package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.ChatMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemoryRepository extends JpaRepository<ChatMemory, Long> {

    void deleteByConversationId(String conversationId);

    boolean existsChatMemoryByConversationId(String conversationId);

    List<ChatMemory> findAllByConversationId(String conversationId);
}
