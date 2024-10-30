package Tests;

import Utils.iOSBusinessProfileFunctions;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class iOSBusinessProfile extends iOSBusinessProfileFunctions {
    IOSDriver driver;
    @Parameters({"platform"})
    @BeforeClass
    public void setup(String platform) throws Throwable {
        driver = (IOSDriver) launchDriver(platform);
        extentReports();
        test = extent.createTest("Check_Country_Code");
    }

    @Test
    public void Login(){
        System.out.println("Logged in successfully");
    }

    @Test
    public void testBusinessProfile() throws Throwable {
        iOSLooper(driver);
    }
    @AfterSuite
    public void tearDown() {
        extent.flush();
    }
    public static void extentReports() {
        spark = new ExtentSparkReporter(reportFolderPath + "/iOS_Setmore" + getCurrentDateTime() + ".html");
        spark.config().setDocumentTitle("Country_code_update");
        spark.config().setReportName("Android Setmore");
        extent.attachReporter(spark);
    }
    public static String getCurrentDateTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Define a custom date and time format if needed
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
        // Format the current date and time using the formatter
        return currentDateTime.format(formatter);
    }
}
