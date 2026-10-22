package pagePackage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationPage 
{
	private WebDriver driver;
    private WebDriverWait wait;
    
    By firstName  = By.id("first_name");
    By lastName   = By.id("last_name");
    By dob        = By.id("dob");
    By street     = By.id("street");
    By postalCode = By.id("postal_code");
    By housenumber= By.id("house_number");
    By city       = By.id("city");
    By state      = By.id("state");
    By country    = By.id("country");
    By phone      = By.id("phone");
    By email      = By.id("email");
    By password   = By.id("password");
    By registerBtn= By.xpath("//button[@data-test='register-submit']");
    By errorMsgs  = By.cssSelector(".alert-danger, .invalid-feedback, [data-test$='-error']");
    
    public RegistrationPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public RegistrationPage open() 
    {
        driver.get("https://practicesoftwaretesting.com/auth/register");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
        return this;
    }
    
    public RegistrationPage fillForm(String fn, String ln, String dobValue, String ctry,String zip,
    		                          String hn,String addr, String cty, String st,  
                                       String ph, String mail, String pwd) 
    {
    	WebElement first_name=driver.findElement(firstName);
        first_name.sendKeys(fn);
        WebElement last_name=driver.findElement(lastName);
        last_name.sendKeys(ln);
        WebElement dateofbirth=driver.findElement(dob);
        dateofbirth.sendKeys(dobValue);
        if (ctry != null && !ctry.trim().isEmpty()) 
        {
            WebElement cntry = wait.until(ExpectedConditions.elementToBeClickable(country));

            new Select(cntry).selectByVisibleText(ctry.trim());
        }
        
        WebElement postal_code=driver.findElement(postalCode);
        postal_code.sendKeys(zip);
        WebElement house_number=driver.findElement(housenumber);
        house_number.sendKeys(hn);
        WebElement strt=driver.findElement(street);
        strt.sendKeys(addr);
        WebElement cityname=driver.findElement(city);
        cityname.sendKeys(cty);
        WebElement statename=driver.findElement(state);
        statename.sendKeys(st);
        WebElement phn=driver.findElement(phone);
        phn.sendKeys(ph);
        WebElement emailid=driver.findElement(email);
        emailid.sendKeys(mail);
        WebElement psswrd=driver.findElement(password);
        psswrd.sendKeys(pwd);
        
        return this;
       
    }
    
    public void submit() 
    {
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public boolean isRegistrationSuccessful() 
    {
        try 
        {
            return wait.until(ExpectedConditions.urlContains("/auth/login"));
        } catch (TimeoutException e)
        {
            return false;
        }
    }

    public boolean hasValidationErrors() 
    {
        return !driver.findElements(errorMsgs).isEmpty();
    }

    public String getErrorText() 
    {
        return driver.findElements(errorMsgs).stream().map(WebElement::getText).reduce("", (a, b) -> a + " " + b).trim();
    }
}
    


