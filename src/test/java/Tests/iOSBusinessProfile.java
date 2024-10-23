package Tests;

import Utils.BusinessProfileFunctions;
import Utils.LoginFunctions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        System.out.println("Business Profile test");
        looper(driver);
//      navigateFromBusinessProfileToCustomerTab(driver);
    }


}
