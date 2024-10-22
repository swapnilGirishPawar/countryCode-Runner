package Tests;

import org.testng.annotations.Test;
import Utils.BusinessProfileFunctions;

public class iOSBusinessProfile extends BusinessProfileFunctions {

    @Test
    public void BusinessProfileUpdate() throws Throwable {
        NavigateToBusinessProfile();
        UpdateCountry();
    }




}
