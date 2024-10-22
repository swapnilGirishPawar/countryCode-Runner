package Tests;

import Utils.BusinessProfileFunctions;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class iOSBusinessProfile extends BusinessProfileFunctions {
    IOSDriver driver;
    @BeforeClass
    public void setup() throws Throwable {
        driver = (IOSDriver) launchDriver("iOS");
    }

    @Test
    public void testBusinessProfile() {
        System.out.println("Business Profile test");
    }

    @Test
    public void BusinessProfileUpdate() throws Throwable {
        NavigateToBusinessProfile();
        UpdateCountry();
    }


}
