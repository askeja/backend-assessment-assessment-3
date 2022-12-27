package com.katanox.api.service.impl;

import com.katanox.api.service.LogInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogWriterService implements LogInterface {

    public void logStringToConsoleOutput(String o) {
        log.warn(o);
    }
}
