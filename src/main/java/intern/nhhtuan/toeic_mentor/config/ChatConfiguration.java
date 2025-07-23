package intern.nhhtuan.toeic_mentor.config;

import intern.nhhtuan.toeic_mentor.advisor.RatingEnabledChatMemoryAdvisor;
import intern.nhhtuan.toeic_mentor.repository.RatingEnabledChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatConfiguration {

    @Bean
    @Primary
    public RatingEnabledChatMemoryRepository chatMemoryRepository(JdbcTemplate jdbcTemplate) {
        return new RatingEnabledChatMemoryRepository(jdbcTemplate);
    }

    @Bean
    public RatingEnabledChatMemoryAdvisor ratingChatMemoryAdvisor(RatingEnabledChatMemoryRepository repository) {
        return RatingEnabledChatMemoryAdvisor.builder(repository)
                .chatMemoryRetrieveSize(100)
                .defaultConversationId("default")
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, RatingEnabledChatMemoryAdvisor advisor) {
        return builder
                .defaultAdvisors(advisor)
                .build();
    }
}
