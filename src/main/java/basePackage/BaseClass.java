package basePackage;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class BaseClass 
{
	
	 protected WebDriver driver;
	 protected final String Base_URL = "https://practicesoftwaretesting.com/";
	 
	    @BeforeMethod
	    public void setup() {

	        driver = new ChromeDriver();
	        driver.get(Base_URL);
	        driver.manage().window().maximize();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    }
	    
	    public void switchToNewWindow() {

	        String currentWindow = driver.getWindowHandle();

	        for(String window : driver.getWindowHandles()) {

	            if(!window.equals(currentWindow)) {

	                driver.switchTo().window(window);
	                break;
	            }
	        }
	    }
	    @AfterMethod
	    public void tearDown() {

	        if (driver != null) {
	            driver.quit();
	        }
	    }


}
