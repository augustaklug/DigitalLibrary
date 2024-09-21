package com.klug.loansystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klug.loansystem.model.UserInfo;
import com.klug.loansystem.repository.UserInfoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserMessageService {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageService.class);

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "userQueue", containerFactory = "tracingRabbitListenerContainerFactory")
    public void receiveUserMessage(String message) throws JsonProcessingException {
        logger.info("Recebida mensagem do RabbitMQ: {}", message);
        try {
            UserInfo userInfo = objectMapper.readValue(message, UserInfo.class);
            logger.info("Mensagem deserializada com sucesso. User ID: {}", userInfo.getId());
            userInfoRepository.save(userInfo);
            logger.info("UserInfo salvo no banco de dados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem do RabbitMQ", e);
            throw e;
        }
    }
}