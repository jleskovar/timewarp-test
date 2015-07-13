package timewarp.test.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.paralleluniverse.vtime.*;

@Controller
public class TimeController {

    private static final Log LOG = LogFactory.getLog(TimeController.class);

    private final DateTimeFormatter DATETIME_FORMATTER = ISODateTimeFormat.dateTimeNoMillis();

    @RequestMapping("/now")
    public ResponseEntity<String> now() {
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    @RequestMapping("/currentTimeMillis")
    public ResponseEntity<String> currentTimeMillis() {
        return new ResponseEntity<>(Long.toString(System.currentTimeMillis()), HttpStatus.OK);
    }

    @RequestMapping("/nanoTime")
    public ResponseEntity<String> nanoTime() {
        return new ResponseEntity<>(Long.toString(System.nanoTime()), HttpStatus.OK);
    }

    @RequestMapping("/pause")
    public ResponseEntity<String> pause() {
        long lastPauseTime = System.currentTimeMillis();
        VirtualClock.setGlobal(new ManualClock(lastPauseTime));
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    @RequestMapping("/fast-forward")
    public ResponseEntity<String> fastForward(@RequestParam(value = "speed", defaultValue = "2.0") String scaleFactor) {
        LOG.info("Fast-forwarding time to be " + scaleFactor + "x normal time");
        VirtualClock.setGlobal(new ScaledClock(new FixedEpochClock(VirtualClock.get().currentTimeMillis()), Double.parseDouble(scaleFactor)));
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    @RequestMapping("/fixed")
    public ResponseEntity<String> fixed(@RequestParam(value = "time", defaultValue = "") String timeToSet) {
        DateTime dateTime = retrieveDateTimeToSet(timeToSet);
        LOG.info(String.format("Fixing time to %d", dateTime.getMillis()));
        VirtualClock.setGlobal(new ManualClock(dateTime.getMillis()));
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    @RequestMapping("/set")
    public ResponseEntity<String> set(@RequestParam(value = "time", defaultValue = "") String timeToSet) {
        DateTime dateTime = retrieveDateTimeToSet(timeToSet);
        LOG.info(String.format("Setting time to %s", dateTime));
        VirtualClock.setGlobal(new FixedEpochClock(dateTime.getMillis()));
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    @RequestMapping("/reset")
    public ResponseEntity<String> reset() {
        VirtualClock.setGlobal(SystemClock.instance());
        LOG.info(String.format("Resetting time to wall clock time of %s", new LocalDateTime().toString()));
        return new ResponseEntity<>(new LocalDateTime().toString(), HttpStatus.OK);
    }

    private DateTime retrieveDateTimeToSet(String timeToSet) {
        DateTime dateTime;
        if (timeToSet.isEmpty()) {
            dateTime = DateTime.now();
        } else if (timeToSet.matches("\\d+")) {
            dateTime = new DateTime(Long.parseLong(timeToSet));
        } else { // ISO-8601 format (yyyy-MM-dd'T'HH:mm:ssZZ)
            dateTime = DATETIME_FORMATTER.parseDateTime(timeToSet);
        }
        return dateTime;
    }

}
