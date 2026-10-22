package testPackage;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.RegistrationPage;
import utilsPackage.ExcelUtility;

public class RegistrationTest extends BaseClass
{
	
	RegistrationPage register;
	String URL="https://practicesoftwaretesting.com/auth/register";
	
	@BeforeMethod
    public void initializePage() 
	{

        register = new RegistrationPage(driver);
        

    }
	
	@DataProvider(name="registrationData")
    public Object[][] loginData() throws IOException 
	{

        ExcelUtility excel = new ExcelUtility("RegistrationData.xlsx","Register");

        try {
            return excel.getExcelData(12);
        } 
        finally {
            excel.closeExcel();
        }
	}
	
	@Test(dataProvider = "registrationData")
	public void verifyRegistration(
	        String fn, String ln, String dob, String country,
	        String zip, String hn, String street, String city,
	        String state, String phone, String email, String pwd,
	        int rowno) throws IOException {

	    ExcelUtility excel =
	            new ExcelUtility("RegistrationData.xlsx", "Register");

	    String actualResult = "FAIL";

	    try {
	        driver.get(URL);

	        register.fillForm(fn, ln, dob, country, zip, hn,
	                street, city, state, phone, email, pwd);

	        register.submit();

	        WebDriverWait wait =
	                new WebDriverWait(driver, Duration.ofSeconds(10));

	        boolean validRegistration = false;

	        if (rowno == 1) {
	            // Only the valid-data row can receive PASS
	            try {
	                wait.until(
	                    ExpectedConditions.urlContains("/auth/login")
	                );

	                validRegistration = true;

	            } catch (TimeoutException e) {
	                validRegistration = false;
	            }
	        }

	        // Invalid-data rows always receive FAIL
	        actualResult = validRegistration ? "PASS" : "FAIL";

	        System.out.println("Excel row: " + (rowno + 1));
	        System.out.println("Actual Result: " + actualResult);
	    }finally {
	        try {
	            excel.writeResult(rowno, actualResult);
	        } finally {
	            excel.closeExcel();
	        }
	    }
	}
	            
}
	
