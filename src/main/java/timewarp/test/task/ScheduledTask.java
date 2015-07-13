package timewarp.test.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private static final Log LOG = LogFactory.getLog(ScheduledTask.class);

    @Scheduled(fixedDelay = 1000L)
    public void runIt() throws InterruptedException {
        LOG.info("Running again. System.cTM is " + System.currentTimeMillis() + " and LDT is " + new LocalDateTime());
    }

}
