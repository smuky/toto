package com.muky.toto.client;

import org.openqa.selenium.chrome.ChromeOptions;

public abstract class IFAClientBase {
    ChromeOptions options;

    protected IFAClientBase() {
        options = getChromeOptions();
    }

    private static ChromeOptions getChromeOptions() {
        // Configure Chrome options for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        options.addArguments("--lang=he-IL");
        return options;
    }
}
