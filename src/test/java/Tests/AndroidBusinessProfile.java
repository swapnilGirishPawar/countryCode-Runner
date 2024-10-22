package Tests;

import BaseClass.Capabilities;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class AndroidBusinessProfile extends Capabilities {
    AndroidDriver driver;

    @BeforeClass
    public void setup() throws Throwable {
        driver = (AndroidDriver) launchDriver("Android");
    }

    @Test
    public void testBusinessProfile() {
        System.out.println("Business Profile test");
    }
}
