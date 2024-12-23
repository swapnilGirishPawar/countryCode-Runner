package BaseClass;

import Utils.CommonUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class Capabilities extends CommonUtils {
    public static ExtentSparkReporter spark;
    public static ExtentTest test;
    public static ExtentTest node;
    public static String reportFolderPath = "reports";
    public static ExtentReports extent = new ExtentReports();

    public static AppiumDriver driver=null;

    public AppiumDriver launchDriver(String platform) throws Throwable {
        DesiredCapabilities capabilities = getCapabilities(platform);

        try {
            if ("Android".equalsIgnoreCase(platform)) {
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            } else if ("iOS".equalsIgnoreCase(platform)) {
                driver = new IOSDriver(new URL("http://127.0.0.1:4723"), capabilities);
            }
            driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 25);
            System.out.println("App launched on " + platform);
        } catch (Exception e) {
            System.err.println("Failed to launch app on " + platform + ": " + e.getMessage());
        }

        return driver;
    }

    private DesiredCapabilities getCapabilities(String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("appium:autoAcceptAlerts", true);
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("appium:appWaitForLaunch", true);

        if ("Android".equalsIgnoreCase(platform)) {
            capabilities.setCapability("deviceName", "RZ8RA1EL07W");
            capabilities.setCapability("platformVersion", "14");
            capabilities.setCapability("appium:automationName", "uiautomator2");
            capabilities.setCapability("appium:platformName", "Android");
            capabilities.setCapability("appium:appPackage", "com.adaptavant.setmore");
            capabilities.setCapability("appium:appActivity", "com.anywhere.container.presentation.ui.MainActivity");
        } else if ("iOS".equalsIgnoreCase(platform)) {
            capabilities.setCapability("deviceName", "Sudhansu phone");
            capabilities.setCapability("platformVersion", "17.5");
            capabilities.setCapability("appium:automationName", "XCUITest");
            capabilities.setCapability("appium:platformName", "iOS");
            capabilities.setCapability("appium:bundleId", "com.setmore.app");
            // iOS capabilities may need adjustment; appPackage and appActivity are Android-specific
        }

        return capabilities;
    }
}
