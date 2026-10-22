package testPackage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import basePackage.BaseClass;
import pagePackage.CartandPaymentPage;
import pagePackage.HomePage;
import pagePackage.ProductPage;
import pagePackage.SignInPage;


public class CartandPaymentTest extends BaseClass

{
	
	String BASE_URL  = "https://practicesoftwaretesting.com/";
    String LOGIN_URL = BASE_URL + "auth/login";
    String PRODUCT   = "Bolt Cutters";
 
    
    String USER_EMAIL    = "sonisam@gmail.com";
    String USER_PASSWORD = "Sam12300##@90";
 
    SignInPage signin;
    HomePage home;
    ProductPage product;
    CartandPaymentPage cart;
 
    @BeforeMethod
    public void initializePages() 
    {
        signin  = new SignInPage(driver);
        home    = new HomePage(driver);
        product = new ProductPage(driver);
        cart    = new CartandPaymentPage(driver);
    }
    
    private void addProductToCart() 
    {
        driver.get(BASE_URL);
        home.executeSearch(PRODUCT);
        product.selectProductByName(PRODUCT);
        product.clickAddToCart();
        cart.waitForCartBadgeAtLeast(1);       
    }
    
    private void ensureLoggedOut() 
    {
        driver.get(BASE_URL);
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }
    
    @Test(priority=1, description = "Signed-in user checks out and pays by credit card")
    public void testSignedInCheckoutWithCreditCard() 
    {
    	driver.get(LOGIN_URL);
        signin.login(USER_EMAIL, USER_PASSWORD);
        cart.waitForUserMenu();

        addProductToCart();
        cart.clickCartIcon();

        Assert.assertTrue(cart.getCartProductTitles().contains(PRODUCT), "Cart should contain " + PRODUCT);
        Assert.assertFalse(cart.getCartTotal().isEmpty(), "Cart total should be present");

        cart.proceedFromCart();

        boolean signedIn = cart.handleSignInStep(
            "guest@example.com", "Guest", "User");
        Assert.assertTrue(signedIn,"User should have been detected as signed in");

        cart.fillBillingAddress(
            "India", "560090", "90", "MG Road", "Bengaluru", "Karnataka");
        cart.proceedToPayment();

        cart.selectPaymentMethod("Credit Card");
        cart.enterCreditCardDetails(
            "1111-1111-1111-1111", "12/2030", "123", "Test User");

        
        String successMsg = cart.confirmPayment();
        Assert.assertTrue(
            successMsg.toLowerCase().contains("payment was successful"),"Payment success message mismatch; found: " + successMsg);

        String orderText = cart.getOrderConfirmationText();
        System.out.println("Order confirmation: " + orderText);
        Assert.assertFalse(orderText.isEmpty(),"Order confirmation should be displayed");
        
        cart.clickCartIcon();

        boolean cartEmpty = cart.isCartEmpty();

        Assert.assertTrue(cartEmpty,"Cart still contains visible rows after checkout.");
    }
	
    @Test(priority=2, description = "Guest user checks out and pays by bank transfer")
    public void testGuestCheckoutWithBankTransfer() 
    {
    	ensureLoggedOut();

        addProductToCart();
        cart.clickCartIcon();
        cart.proceedFromCart();

        boolean signedIn = cart.handleSignInStep(
            "bonnymathew@gmail.com", "Bonny", "Mathew");
        Assert.assertFalse(
            signedIn,
            "Guest flow should have been used");

        cart.fillBillingAddress(
            "India", "560090", "90", "MG Road", "Bengaluru", "Karnataka");
        cart.proceedToPayment();

        cart.selectPaymentMethod("Bank Transfer");
        cart.enterBankTransferDetails(
            "HDFC", "Bonny Mathew", "12345678");

        String successMsg = cart.confirmPayment();
        Assert.assertTrue(
            successMsg.toLowerCase().contains("payment was successful"),
            "Payment success message mismatch; found: " + successMsg);

        String orderText = cart.getOrderConfirmationText();
        System.out.println("Order confirmation: " + orderText);
        Assert.assertFalse(
            orderText.isEmpty(),
            "Order confirmation should be displayed");
        
        cart.clickCartIcon();

        boolean cartEmpty = cart.isCartEmpty();

        Assert.assertTrue(cartEmpty,"Cart still contains visible rows after checkout.");
    }
    
   
    @Test(priority = 3, description = "Negative: Verify checkout is blocked when cart quantity is zero")
    	public void testCheckoutWithZeroQuantity() {

    	    ensureLoggedOut();
    	    addProductToCart();
    	    cart.clickCartIcon();

    	    cart.setCartQuantityToZero();

    	    String quantity = cart.getCartQuantityValue();

    	    if (!"0".equals(quantity)) 
    	    {
    	    	Assert.assertTrue(Integer.parseInt(quantity) > 0, "Unexpected cart quantity: " + quantity);
    	        return;
    	    }

    	    cart.proceedFromCart();

    	    boolean validationDisplayed =cart.isCartValidationMessageDisplayed();

    	    boolean stillOnCart =!driver.findElements(By.cssSelector("[data-test='proceed-1']")).isEmpty();

    	    Assert.assertTrue( validationDisplayed || stillOnCart,
    	        "Security/validation defect: Checkout proceeded with " +
    	        "zero quantity without displaying an error.");
    	}
    
}
