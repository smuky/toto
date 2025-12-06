package com.muky.toto.client;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class WebDriverPool {
    
    private WebDriver driver;
    private final ReentrantLock lock = new ReentrantLock();
    private final ChromeOptions options;
    
    public WebDriverPool() {
        this.options = getChromeOptions();
        log.info("WebDriverPool initialized");
    }
    
    public WebDriver getDriver() {
        lock.lock();
        try {
            if (driver == null) {
                log.info("Creating new WebDriver instance");
                driver = new ChromeDriver(options);
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            }
            return driver;
        } finally {
            lock.unlock();
        }
    }
    
    public void releaseDriver() {
        // No-op - driver is reused
        // Just unlock if needed
    }
    
    @PreDestroy
    public void cleanup() {
        lock.lock();
        try {
            if (driver != null) {
                log.info("Shutting down WebDriver");
                driver.quit();
                driver = null;
            }
        } finally {
            lock.unlock();
        }
    }
    
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        options.addArguments("--lang=he-IL");
        return options;
    }
}
