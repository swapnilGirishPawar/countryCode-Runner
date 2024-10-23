//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class test {
//
//    // WebDriver instance
//    WebDriver driver;
//
//    // List to keep track of failed countries
//    List<String> failedCountries = new ArrayList<>();
//
//    public static void main(String[] args) throws Exception {
//        CountryValidation validator = new CountryValidation();
//        validator.runValidation();
//    }
//
//    public void runValidation() throws Exception {
//        // Initialize WebDriver (you may need to set the path to your driver)
//        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
//        driver = new ChromeDriver();
//
//        // Read country list from JSON
//        JsonArray countries = readCountriesFromJson();
//
//        // Login with credentials
//        login();
//
//        // Iterate over all countries from the JSON file
//        for (int i = 0; i < countries.size(); i++) {
//            JsonObject countryObj = countries.get(i).getAsJsonObject();
//            String countryName = countryObj.get("country").getAsString();
//
//            // Step 2 to Step 6: Navigate to the business profile and update country
//            navigateToBusinessProfileAndUpdateCountry(countryName);
//
//            // Step 7 to Step 9: Validate country in customer creation flow
//            if (!validateCustomerCountry(countryName)) {
//                failedCountries.add(countryName);
//            }
//
//            // Navigate back to calendar page
//            navigateToCalendarPage();
//        }
//
//        // Output the failed countries
//        if (failedCountries.isEmpty()) {
//            System.out.println("All countries matched successfully!");
//        } else {
//            System.out.println("Countries that failed to match: " + failedCountries);
//        }
//
//        // Quit driver
//        driver.quit();
//    }
//
//    public JsonArray readCountriesFromJson() throws Exception {
//        FileReader reader = new FileReader("phonenumber.json");
//        return JsonParser.parseReader(reader).getAsJsonArray();
//    }
//
//    public void login() {
//        // Assuming you are on the login page
//        driver.get("https://your-application-url.com/login
//                ");
//                WebElement username = driver.findElement(By.id("username"));
//        WebElement password = driver.findElement(By.id("password"));
//        WebElement loginButton = driver.findElement(By.id("loginButton"));
//
//        // Enter credentials
//        username.sendKeys("yourUsername");
//        password.sendKeys("yourPassword");
//        loginButton.click();
//    }
//
//    public void navigateToBusinessProfileAndUpdateCountry(String countryName) throws InterruptedException {
//        // Navigate to Settings tab
//        WebElement settingsTab = driver.findElement(By.id("settingsTab"));
//        settingsTab.click();
//
//        // Go to Business Profile Edit View
//        WebElement businessProfileEdit = driver.findElement(By.id("businessProfileEdit"));
//        businessProfileEdit.click();
//
//        // Search for country in the country field and select it
//        WebElement countryField = driver.findElement(By.id("countryField"));
//        countryField.clear();
//        countryField.sendKeys(countryName);
//        WebElement countryDropdown = driver.findElement(By.xpath("//option[contains(text(), '" + countryName + "')]"));
//        countryDropdown.click();
//
//        // Save the changes
//        WebElement saveButton = driver.findElement(By.id("saveButton"));
//        saveButton.click();
//
//        // Wait for save confirmation
//        Thread.sleep(2000);
//    }
//
//    public boolean validateCustomerCountry(String countryName) throws InterruptedException {
//        // Navigate to Customer tab
//        WebElement customerTab = driver.findElement(By.id("customerTab"));
//        customerTab.click();
//
//        // Go to Create New Customer flow
//        WebElement createNewCustomerButton = driver.findElement(By.id("createNewCustomer"));
//        createNewCustomerButton.click();
//
//        // Check if the country matches
//        WebElement customerCountryField = driver.findElement(By.id("customerCountryField"));
//        String selectedCountry = customerCountryField.getAttribute("value");
//
//        // Validate if the country matches
//        boolean isMatch = selectedCountry.equals(countryName);
//
//        // Output result
//        if (isMatch) {
//            System.out.println("Pass: Country matches for " + countryName);
//        } else {
//            System.out.println("Fail: Country mismatch for " + countryName + ". Expected: " + countryName + ", Found: " + selectedCountry);
//        }
//
//        // Wait for the operation to complete
//        Thread.sleep(1000);
//
//        return isMatch;
//    }
//
//    public void navigateToCalendarPage() {
//        WebElement calendarTab = driver.findElement(By.id("calendarTab"));
//        calendarTab.click();
//    }
//}’