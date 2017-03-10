package gov.onc.xdrtesttool.services;

import gov.onc.xdrtesttool.entities.MessageLog;
import gov.onc.xdrtesttool.repositories.MessageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Brian on 3/9/2017.
 */
@Service
public class MessageLogService {
    private MessageLogRepository messageLogRepository;

    @Autowired
    public MessageLogService(MessageLogRepository messageLogRepository) {
        this.messageLogRepository = messageLogRepository;
    }

    public MessageLog saveLog(MessageLog messageLog){
        return messageLogRepository.save(messageLog);
    }

    public List<MessageLog> getLogsByFromAddress(String fromAddress){
        return messageLogRepository.findByFromAddressOrderByCreatedDesc(fromAddress);
    }

    public List<MessageLog> getLogsByIpAddress(String ipAddress) {
        return messageLogRepository.findByIpAddressOrderByCreatedDesc(ipAddress);
    }

}
