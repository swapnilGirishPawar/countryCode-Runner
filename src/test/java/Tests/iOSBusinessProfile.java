package Tests;

import BaseClass.Capabilities;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class iOSBusinessProfile extends Capabilities {
    IOSDriver driver;
    @BeforeClass
    public void setup() throws Throwable {
        driver = (IOSDriver) launchDriver("iOS");
    }

    @Test
    public void testBusinessProfile() {
        System.out.println("Business Profile test");
    }
}
