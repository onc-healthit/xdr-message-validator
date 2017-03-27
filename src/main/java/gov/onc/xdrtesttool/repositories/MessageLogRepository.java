package gov.onc.xdrtesttool.repositories;

import gov.onc.xdrtesttool.entities.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Brian on 3/9/2017.
 */
@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long>{
    List<MessageLog>findByIpAddressOrderByCreatedDesc(String ipAddress);
    List<MessageLog>findByFromAddressOrderByCreatedDesc(String fromAddress);
}
