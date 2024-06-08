package com.example.firstprogramInspring.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;

@Configuration
public class LoggerConfig{

	private static final String LOG_ROOT = "logs";
    private static final String LOG_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

    @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();

        ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(context);
        consoleAppender.start();

        Map<String, RollingFileAppender<ILoggingEvent>> packageAppenders = createPackageAppenders(context);

        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(consoleAppender);

        for (Map.Entry<String, RollingFileAppender<ILoggingEvent>> entry : packageAppenders.entrySet()) {
        	
            Logger packageLogger = (Logger) LoggerFactory.getLogger(entry.getKey());
            packageLogger.setLevel(Level.INFO);
            packageLogger.setAdditive(true); 
            packageLogger.addAppender(entry.getValue());
        }
        
        
    }
    private ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext context) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
//        appender.setImmediateFlush(true);
        appender.setContext(context);
     
        return appender;
    }

    private Map<String, RollingFileAppender<ILoggingEvent>> createPackageAppenders(LoggerContext context) {
        Map<String, RollingFileAppender<ILoggingEvent>> appenders = new HashMap<>();
        String[] packages = {"com.example.firstprogramInspring", "com.example.firstprogramInspring.controller",
                "com.example.firstprogramInspring.service", "com.example.firstprogramInspring.Model",
                "com.example.firstprogramInspring.DAO", "com.example.firstprogramInspring.DAOimpl"};

        for (String packageName : packages) {
            RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
            fileAppender.setContext(context);
        
            
            fileAppender.setFile(LOG_ROOT + "/" + packageName.replace('.', '/') + ".log");

            SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
            rollingPolicy.setContext(context);
            rollingPolicy.setParent(fileAppender);
            rollingPolicy.setFileNamePattern(LOG_ROOT + "/" + packageName.replace('.', '/') + "-%d{yyyy-MM-dd}.%i.log.gz");
            rollingPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
            rollingPolicy.setMaxHistory(30);
            rollingPolicy.setTotalSizeCap(FileSize.valueOf("100GB"));
            rollingPolicy.start();
            
            PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
            fileEncoder.setContext(context);
            fileEncoder.setPattern(LOG_PATTERN);
            fileEncoder.start();

            fileAppender.setEncoder(fileEncoder);
            fileAppender.setRollingPolicy(rollingPolicy);
            fileAppender.start();

            appenders.put(packageName, fileAppender);
        }
        return appenders;
    }
	
	
}

