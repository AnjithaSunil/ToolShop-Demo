package testPackage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.CartandPaymentPage;
import pagePackage.SignInPage;

public class EndToEndTest extends BaseClass

{

	 WebDriverWait wait;
     CartandPaymentPage cart;
     
     String EMAIL ="sonisam@gmail.com";
     String PASSWORD = "Sam12300##@90";
     
     @BeforeMethod
     public void setUpEndToEnd() 
     {
         wait = new WebDriverWait(driver, Duration.ofSeconds(15));
         cart = new CartandPaymentPage(driver);
     }
     
     @Test(priority=1, description="Happy flow")
     public void testEndToEndHappyFlow() 
     {

         // Step 1: Sign in
         driver.findElement(By.cssSelector("[data-test='nav-sign-in']")).click();

         wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']"))).sendKeys(EMAIL);

         driver.findElement(By.cssSelector("[data-test='password']")).sendKeys(PASSWORD);

         driver.findElement(By.cssSelector("[data-test='login-submit']")).click();
         
         wait.until(ExpectedConditions.urlContains("/account"));

         Assert.assertTrue(driver.getCurrentUrl().contains("/account"),"URL should contain /account after successful sign-in");
         
         wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@data-test='nav-home']"))).click();

         WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable( By.cssSelector("[data-test='product-name']")));
         firstProduct.click();
         
         wait.until(ExpectedConditions.elementToBeClickable( By.cssSelector("[data-test='add-to-cart']"))).click();
         
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
         
         Assert.assertTrue(
                 wait.until(ExpectedConditions.visibilityOfElementLocated(
                         By.cssSelector("[data-test='product-title']"))).isDisplayed(),"Product was not added to cart");
         
         cart.proceedFromCart();
         
         Assert.assertTrue(cart.isUserSignedIn() ,"User is not signed in at checkout");
         cart.proceedAsSignedInUser(); 
         
         cart.fillBillingAddress("India", "560090", "90", "MG Road", "Bengaluru", "Karnataka");
         cart.proceedToPayment();
         cart.selectPaymentMethod("Cash on Delivery");
         cart.confirmPayment();
         
         String confirmation = cart.getOrderConfirmationText();

         Assert.assertNotNull(confirmation,"Order confirmation was not returned");

         Assert.assertFalse(confirmation.trim().isEmpty(),"Order confirmation is empty");
         
     }
     
     @Test(priority=2, description="Negative: Invalid login")
     
     public void testLoginWithInvalidCredentials() {

         driver.findElement(By.cssSelector("[data-test='nav-sign-in']")).click();

         wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']"))).sendKeys("invalid_user@example.com");
         driver.findElement(By.cssSelector("[data-test='password']") ).sendKeys("WrongPassword123");
         driver.findElement(By.cssSelector("[data-test='login-submit']")).click();

         WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='login-error']")));
         Assert.assertTrue(errorMessage.isDisplayed(),"Expected login error was not displayed");
         Assert.assertTrue(driver.findElements(By.cssSelector("[data-test='nav-sign-out']")).isEmpty(),
                 "Invalid credentials unexpectedly signed in the user");
         
     }
 
     @Test(priority = 3, description = "REQUIRED BILLING FIELD MISSING")
     public void testCheckoutWithMissingRequiredField() {

         SignInPage signin = new SignInPage(driver);

         signin.clicksignin();
         signin.login(EMAIL, PASSWORD);

         wait.until(ExpectedConditions.urlContains("/account"));

         Assert.assertTrue(
             driver.getCurrentUrl().contains("/account"),"URL should contain /account after successful sign-in");

         // Navigate to the home page.
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-home']"))).click();

         // Select a product.
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-name']"))).click();

         // Add the product to the cart.
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();

         // Open the cart.
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

         Assert.assertTrue( wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.cssSelector("[data-test='product-title']"))).isDisplayed(),"Product was not added to cart");

         // Proceed to the billing/address step.
         cart.proceedFromCart();

         Assert.assertTrue(cart.isUserSignedIn(),"User is not signed in at checkout");

         cart.proceedAsSignedInUser();

         WebElement postal_code=driver.findElement(By.id("postal_code"));
         postal_code.clear(); 
         
         Assert.assertTrue(
        		    postal_code.getAttribute("value") == null
        		        || postal_code.getAttribute("value").trim().isEmpty(),"Street field should be empty for the negative test");

        		// Locate the Continue/Payment button.
        		By paymentButtonLocator =
        		    By.cssSelector("button[data-test='proceed-3']");

        		WebElement paymentButton = wait.until(ExpectedConditions.presenceOfElementLocated(paymentButtonLocator));

        		Assert.assertFalse(
        		    paymentButton.isEnabled(),"Payment button should be disabled when required billing data is missing");
          
     }
}
