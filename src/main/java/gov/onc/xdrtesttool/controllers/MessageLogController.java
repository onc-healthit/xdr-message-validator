package gov.onc.xdrtesttool.controllers;

import gov.onc.xdrtesttool.entities.MessageLog;
import gov.onc.xdrtesttool.services.MessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Brian on 3/9/2017.
 */
@RestController
public class MessageLogController {
    private MessageLogService messageLogService;

    @Autowired
    public MessageLogController(MessageLogService messageLogService) {
        this.messageLogService = messageLogService;
    }

    @RequestMapping(value = "/getlogsbyfromaddress", method = RequestMethod.GET)
    public List<MessageLog> getLogsByFromAddress(@RequestParam(value = "fromAddress") String fromAddress){
        return messageLogService.getLogsByFromAddress(fromAddress);
    }

    @RequestMapping(value = "/getlogsbyipaddress", method = RequestMethod.GET)
    public List<MessageLog> getLogsByIpAddress(@RequestParam(value = "ipAddress") String ipAddress){
        return messageLogService.getLogsByIpAddress(ipAddress);
    }
}
