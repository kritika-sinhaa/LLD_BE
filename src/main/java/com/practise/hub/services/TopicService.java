package com.practise.hub.services;

import com.practise.hub.entities.Topic;
import com.practise.hub.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic getTopicById(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (!topic.isPresent()) {
            throw new RuntimeException("Topic not found with id: " + id);
        }
        return topic.get();
    }
} 