package testPackage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.SignInPage;

public class SignInTest extends BaseClass
{
	SignInPage signin;
	
	@BeforeMethod
	  public void initializePage() 
	  {
		  signin= new SignInPage(driver);
		  signin.clicksignin();
	  }
	
	@Test(priority = 1, description = "Verify successful login with valid customer credentials")
    public void testSuccessfulLogin() 
	{
        
        signin.enterEmail("sonisam@gmail.com");
        signin.enterPassword("Sam12300##@90");
        signin.clickLogin();

        String actualTitle = signin.getPageTitleAfterLogin();
        Assert.assertTrue( actualTitle.contains("Overview"), 
        		"Login failed or user was not navigated to the Overview page. Actual title: " + actualTitle );
    }
	
	@Test(priority = 2, description = "Verify error message on invalid password")
    public void testInvalidPasswordLogin() 
	{
        signin.enterEmail("anjitha@gmail.com");
        signin.enterPassword("anhjm123");
        signin.clickLogin();

        String errorMsg = signin.getLoginErrorMessage();
        Assert.assertTrue(errorMsg.contains("Invalid email or password"), 
                "Error alert should display invalid credentials message.");
    }
	
	@Test(priority = 3, description = "Verify inline error validation on empty input fields")
    public void testEmptyFieldsValidation() 
	{
        signin.clickLogin();

        Assert.assertEquals(signin.getEmailValidationError(), "Email is required", 
                "Email field should trigger required error.");
        Assert.assertEquals(signin.getPasswordValidationError(), "Password is required", 
                "Password field should trigger required error.");
    }
	
	@Test(priority = 4, description = "Verify navigation link to Registration Page")
    public void testNavigateToRegisterPage() 
	{
        signin.clickRegisterLink();

        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/register"), 
                "User should be navigated to the Registration page.");
    }
	
	@Test(priority = 5, description = "Verify navigation link to Forgot Password Page")
	
	public void testNavigateToForgotPasswordPage()
	{
		signin.clickForgotPsswdLink();
		 Assert.assertTrue(driver.getCurrentUrl().contains("/auth/forgot-password"), 
	                "User should be navigated to the Password Reset page.");
	}
	
	@Test(priority = 6, description = "Verify successful logout")
	
	public void testSuccessfulLogout() 
	{
		signin.login("sonisam@gmail.com","Sam12300##@90" );
		signin.clickUserMenu();
		
		Assert.assertTrue( signin.isLogoutLinkDisplayed(), "Sign out link should be displayed after login." );
		
		signin.clickLogout(); 
		Assert.assertTrue( signin.isSignInLinkDisplayed(), "Sign-in link should be displayed after logout." );
	}
	

}
