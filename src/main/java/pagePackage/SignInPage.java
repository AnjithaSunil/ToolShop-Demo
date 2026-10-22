package pagePackage;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignInPage 
{
	private WebDriver driver;
    private WebDriverWait wait;
    
	By signin=By.xpath("//a[@data-test='nav-sign-in']");
	By email=By.id("email");
	By passwordinput=By.id("password");
	By submit=By.className("btnSubmit");
	By registerlink=By.xpath("//a[@data-test='register-link']");
	By email_alert=By.xpath("//div[@data-test='email-error']");
	By password_alert=By.id("password-error");
	By login_alert=By.xpath("//div[@data-test='login-error']");
	By user_menu = By.cssSelector("button[data-test='nav-menu']");
	By logout = By.cssSelector("a[data-test='nav-sign-out']");
	By forgot_psswd=By.xpath("//a[@data-test='forgot-password-link']");
	
	
	public SignInPage(WebDriver driver) 
	{
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
	
	public void clicksignin()
	{
		WebElement signinclick= driver.findElement(signin);
		signinclick.click();
	}
	
	public void enterEmail(String emailid) 
	{
		WebElement email_input=driver.findElement(email);
        wait.until(ExpectedConditions.visibilityOf(email_input)).clear();
        email_input.sendKeys(emailid);
    }
	
	public void enterPassword(String password) 
	{
		WebElement password_input=driver.findElement(passwordinput);
        password_input.clear();
        password_input.sendKeys(password);
    }
	
	public void clickLogin() 
	{
		WebElement loginSubmitBtn=driver.findElement(submit);
        wait.until(ExpectedConditions.elementToBeClickable(loginSubmitBtn)).click();
    }
	
	public void login(String emailid, String password)
	{
		WebElement email_input=driver.findElement(email);
		wait.until(ExpectedConditions.visibilityOf(email_input));
		enterEmail(emailid);
		enterPassword(password);
		clickLogin();
	}
	
	public void clickUserMenu() 
	{ 
		wait.until( ExpectedConditions.elementToBeClickable(user_menu) ).click(); 
	}
	
	public void clickLogout() 
	{
		
		wait.until(ExpectedConditions.elementToBeClickable(logout)).click(); 
	}
	
	
	public boolean isLogoutLinkDisplayed() 
	{
		try 
		{ 
			return wait.until( ExpectedConditions.visibilityOfElementLocated(logout) ).isDisplayed(); 
			} catch (TimeoutException e) 
		{ 
				return false;
		}
	}
	
	public boolean isSignInLinkDisplayed() 
	{ 
		return wait.until( ExpectedConditions.visibilityOfElementLocated(signin) ).isDisplayed(); 
	}
	
	public String getPageTitleAfterLogin() 
	{
	    wait.until(ExpectedConditions.titleContains("Overview"));
	    return driver.getTitle();
	}
	
	public boolean isPageTitleMyAccount() 
	{
	    try 
	    {
	        return wait.until(ExpectedConditions.titleIs("Overview"));
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public String getLoginErrorMessage() 
	{
		WebElement login_error=driver.findElement(login_alert);
        return wait.until(ExpectedConditions.visibilityOf(login_error)).getText().trim();
    }

    public String getEmailValidationError() 
    {
    	WebElement email_error=driver.findElement(email_alert);
        return wait.until(ExpectedConditions.visibilityOf(email_error)).getText().trim();
    }

    public String getPasswordValidationError() 
    {
    	WebElement password_error=driver.findElement(password_alert);
        return wait.until(ExpectedConditions.visibilityOf(password_error)).getText().trim();
    }
    
    public void clickRegisterLink()
    {
    	WebElement click_register= driver.findElement(registerlink);
    	click_register.click();
    }
    
    public void clickForgotPsswdLink()
    {
    	WebElement frgt_psswd=driver.findElement(forgot_psswd);
    	frgt_psswd.click();
    }
	
	

}
