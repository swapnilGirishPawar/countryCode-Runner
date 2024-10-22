package BaseClass;

import Utils.CommonUtils;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Capabilities extends CommonUtils {
    static String deviceName;
    static String platformVersion;
    static String platformName;
    static String udid;
    static String appPackage;
    static String appActivity;
    static String automationName;
    static String bundleId;
    static String xcodeSigningId;
    static String xcodeOrgId;


    static {
        try {
            automationName = ReadProperties("automationName", AppConfigFilePath);
            platformName = ReadProperties("platformName", AppConfigFilePath);
            deviceName = ReadProperties("deviceName", AppConfigFilePath);
            platformVersion = ReadProperties("platformVersion", AppConfigFilePath);
            bundleId = ReadProperties("bundleId", AppConfigFilePath);
            udid = ReadProperties("udid", AppConfigFilePath);
            appPackage = ReadProperties("appPackage", AppConfigFilePath);
            appActivity = ReadProperties("appActivity", AppConfigFilePath);
            xcodeSigningId = ReadProperties("xcodeSigningId", AppConfigFilePath);
            xcodeOrgId = ReadProperties("xcodeOrgId", AppConfigFilePath);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

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
        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
        androidDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 25);
        System.out.println("launch");
    }




    public static void LaunchAppInIos() throws MalformedURLException {
        System.out.println(" "+automationName+" "+platformName+" "+deviceName+" "+platformName+" "+bundleId);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName",automationName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("appium:platformVersion", platformVersion);
        capabilities.setCapability("bundleId", bundleId);
        capabilities.setCapability("noReset",true);
        capabilities.setCapability("appium:appWaitForLaunch",true);
        capabilities.setCapability("autoAcceptAlerts",true);
        capabilities.setCapability("appium:autoDismissKeyboard", true);
        iosDriver = new IOSDriver(new URL("http://0.0.0.0:4723/"), capabilities);
        iosDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }
}
