package Tests;

import BaseClass.Capabilities;
import Utils.BusinessProfileFunctions;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class AndroidBusinessProfile extends BusinessProfileFunctions {
    AndroidDriver driver;

    @BeforeClass
    public void setup() throws Throwable {
        driver = (AndroidDriver) launchDriver("Android");
    }
    @Test
    public void Login(){
        System.out.println("Logged in successfully");
    }

    @Test
    public void testBusinessProfile() throws Throwable {
        this.looper(this.driver);
    }
}
