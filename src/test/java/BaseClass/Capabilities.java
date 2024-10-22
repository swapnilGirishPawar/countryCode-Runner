package BaseClass;

import Utils.CommonUtils;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class Capabilities extends CommonUtils {

    public static void LaunchAppInAndroid() throws Throwable {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:autoAcceptAlerts", true);
        capabilities.setCapability("deviceName", "RZ8T31C2GFH");
        capabilities.setCapability("platformVersion", "14");
        capabilities.setCapability("appium:automationName", "uiautomator2");
        capabilities.setCapability("appium:platformName", "Android");
        capabilities.setCapability("appium:appPackage", "com.adaptavant.setmore");
        capabilities.setCapability("appium:appActivity", "com.anywhere.container.presentation.ui.MainActivity");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("appium:appWaitForLaunch", true);
//        capabilities.setCapability("appium:ignoreHiddenApiPolicyError", true);
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
        driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 25);
        System.out.println("launch");
    }
}
