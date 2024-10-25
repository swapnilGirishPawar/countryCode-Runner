package Tests;

import io.appium.java_client.android.AndroidDriver;
import Utils.AndroidBusinessProfileFunctions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class AndroidBusinessProfile extends AndroidBusinessProfileFunctions {
    AndroidDriver driver;

    @Parameters({"platform"})
    @BeforeClass
    public void setup(String platform) throws Throwable {
        driver = (AndroidDriver) launchDriver(platform);
    }

    @Test
    public void Login(){
        System.out.println("Logged in successfully");
    }

    @Test
    public void testBusinessProfile() throws Throwable {
        AndroidLooper(driver);
    }
    public AndroidBusinessProfile() throws Exception {
    }
}
