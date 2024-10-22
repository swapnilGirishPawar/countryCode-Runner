package Tests;

import Utils.BusinessProfileFunctions;
import Utils.LoginFunctions;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class iOSBusinessProfile extends BusinessProfileFunctions {
    LoginFunctions login = new LoginFunctions();
    IOSDriver driver;
    @Parameters({"platform"})
    @BeforeClass
    public void setup(String platform) throws Throwable {
        driver = (IOSDriver) launchDriver(platform);
    }

    @Test
    public void Login(){
        System.out.println("Logged in successfully");
    }

    @Test
    public void testBusinessProfile() throws Throwable {
//        login.emailLogin(driver);
        System.out.println("Business Profile test");
        NavigateToBusinessProfile(driver);
        UpdateCountry(driver);
    }

//    @Test
//    public void BusinessProfileUpdate() throws Throwable {
//        NavigateToBusinessProfile();
//        UpdateCountry();
//    }


}
