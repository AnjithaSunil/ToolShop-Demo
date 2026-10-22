package pagePackage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactPage

{
	private WebDriver driver;
    private WebDriverWait wait;
	
	 By contact = By.linkText("Contact");
	 By firstname = By.id("first_name");
	 By lastname = By.id("last_name");
	 By email = By.xpath("//input[@formcontrolname='email']");
	 By subject = By.xpath("//select[@id='subject']");
	 By message = By.xpath("//textarea[@id='message']");
	 By attach = By.xpath("//input[@id='attachment']");
	 By submit = By.xpath("//input[@type='submit']");
	 By success = By.xpath("//div[contains(@class,'alert-success')]");
	 By alert = By.id("email_alert");
	 By message_alert = By.id("message_alert");
	 
	 public ContactPage(WebDriver driver) 
	 {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	      
	    }
	 
	 public void contactpageload()
	 {
		 WebElement contact_click=driver.findElement(contact);
		 contact_click.click();
	 }
	 
	 public boolean isContactFormDisplayed() 
	 {
		    return wait.until(ExpectedConditions.visibilityOfElementLocated(firstname)).isDisplayed();
		}
	    
	 
	 public void enterFirstName(String firstName) 
	 {
		    WebElement first_name=driver.findElement(firstname);
	        wait.until(ExpectedConditions.visibilityOf(first_name)).clear();
	        first_name.sendKeys(firstName);
	    }

	    public void enterLastName(String lastName) 
	    {
	    	WebElement last_name=driver.findElement(lastname);
	        last_name.clear();
	        last_name.sendKeys(lastName);
	    }

	    public void enterEmail(String emailid) 
	    {
	    	WebElement email_input=driver.findElement(email);
	        email_input.clear();
	        email_input.sendKeys(emailid);
	    }
	    
	    public void selectSubject(String subjectText) 
	    {
	    	WebElement subject_text=driver.findElement(subject);
	        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(subject_text)));
	        select.selectByVisibleText(subjectText);
	    }

	    public void enterMessage(String Message) 
	    {
	    	WebElement message_text=driver.findElement(message);
	        message_text.clear();
	        message_text.sendKeys(Message);
	    }
	    
	    public void clickSubmit() 
	    {
	    	WebElement submitbutton = driver.findElement(submit);
	        wait.until(ExpectedConditions.elementToBeClickable(submitbutton)).click();
	    }
	    
	    
	    public boolean isSuccessAlertDisplayed() 
	    {
	    	WebElement success_alert=driver.findElement(success);
	        return wait.until(ExpectedConditions.visibilityOf(success_alert)).isDisplayed();
	    }

	    public String getSuccessAlertText() 
	    {
	    	WebElement success_alert=driver.findElement(success);
	        return wait.until(ExpectedConditions.visibilityOf(success_alert)).getText().trim();
	    }

	    public String getEmailErrorText() 
	    {
	    	WebElement email_alert=driver.findElement(alert);
	        return wait.until(ExpectedConditions.visibilityOf(email_alert)).getText().trim();
	    }
	    
	    public String getMessageErrorText() 
	    {
	    	WebElement messagealert=driver.findElement(message_alert);
	        return wait.until(ExpectedConditions.visibilityOf(messagealert)).getText().trim();
	    }

	    
	    public boolean isMessageErrorDisplayed() 
	    {
	    	WebElement messagealert=driver.findElement(message_alert);
	        try {
	            return wait.until(ExpectedConditions.visibilityOf(messagealert)).isDisplayed();
	        } catch (Exception e) 
	        {
	            return false;
	        }
	    }

}
