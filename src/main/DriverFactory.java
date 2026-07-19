private static void initDriver() {
    io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

    String browser = ConfigManager.getInstance().getBrowser().toLowerCase();
    WebDriver driver;

    switch (browser) {
        case "firefox":
            FirefoxOptions ffOptions = new FirefoxOptions();
            if (ConfigManager.getInstance().isHeadless()) {
                ffOptions.addArguments("--headless");
            }
            driver = new FirefoxDriver(ffOptions);
            break;

        case "edge":
            driver = new EdgeDriver();
            break;

        case "chrome":
        default:
            ChromeOptions chromeOptions = new ChromeOptions();
            if (ConfigManager.getInstance().isHeadless()) {
                chromeOptions.addArguments("--headless=new");
            }
            driver = new ChromeDriver(chromeOptions);
            break;
    }

    driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigManager.getInstance().getImplicitWaitSeconds()));
    driver.manage().window().maximize();

    driverThreadLocal.set(driver);
}