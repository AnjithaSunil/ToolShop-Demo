package pagePackage;

import java.time.Duration;
import java.util.List;

import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartandPaymentPage 

{
	private WebDriver driver;
    private WebDriverWait wait;
    
    By userMenu=By.cssSelector("[data-test='nav-menu']");
    By cartIcon=By.cssSelector("a[data-test='nav-cart']");
    By cartBadge=By.cssSelector("[data-test='cart-quantity']");
    By cartRows=By.cssSelector("table tbody tr");
    By cartProductTitle=By.cssSelector("span[data-test='product-title']");
    By cartTotal=By.cssSelector("[data-test='cart-total']");
    By proceedFromCart=By.cssSelector("[data-test='proceed-1']");
 
    // ---------- Step 2: sign in / guest ----------
    By signedInProceed=By.cssSelector("button[data-test='proceed-2']");
    By guestTab=By.linkText("Continue as Guest");
    By guestEmail=By.id("guest-email");
    By guestFirstName=By.id("guest-first-name");
    By guestLastName=By.id("guest-last-name");
    By guestSubmit=By.cssSelector("[data-test='guest-submit']");
    By guestProceed=By.cssSelector("[data-test='proceed-2-guest']");
 
    // ---------- Step 3: billing address ----------
    By countrySelect=By.cssSelector("select[data-test='country']");
    By postalCode=By.cssSelector("input[data-test='postal_code']");
    By houseNumber=By.cssSelector("input[data-test='house_number']");
    By street=By.cssSelector("input[data-test='street']");
    By city=By.cssSelector("input[data-test='city']");
    By state=By.cssSelector("input[data-test='state']");
    By proceedToPayment=By.cssSelector("button[data-test='proceed-3']");
 
    // ---------- Step 4: payment ----------
    By paymentMethod=By.cssSelector("#payment-method, [data-test='payment-method']");
    By bankName=By.cssSelector("#bank_name, [data-test='bank_name']");
    By accountName=By.cssSelector("#account_name, [data-test='account_name']");
    By accountNumber=By.cssSelector("#account_number, [data-test='account_number']");
    By cardNumber=By.id("credit_card_number");
    By cardExpiry=By.id("expiration_date");
    By cardCvv=By.id("cvv");
    By cardHolder=By.id("card_holder_name");
    By checkPaymentBtn = By.xpath("//button[normalize-space()='Check payment']");
    By confirmBtn=By.xpath("//button[normalize-space()='Confirm']");
    By successMessage=By.cssSelector("[data-test='payment-success-message']");
    By orderConfirmation = By.cssSelector("#order-confirmation, [data-test='payment-success-message']");
    By cartQuantityInput=By.cssSelector("input[data-test='product-quantity']");
    By cartValidationMessage=By.cssSelector(".invalid-feedback, .alert-danger, " +".toast-error, [role='alert']");
    

    public CartandPaymentPage(WebDriver driver)
    {
    	  this.driver = driver;
	      this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	      
    }
    
    private void click(By locator) 
    {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
 
    private void type(By locator, String value) 
    {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }
 
    private String text(By locator) 
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }
 
    private boolean isPresentAndVisible(By locator) 
    {
        List<WebElement> els = driver.findElements(locator);
        return !els.isEmpty() && els.get(0).isDisplayed();
    }
 
    private void typeIfPresent(By locator, String value) 
    {
        if (value != null && isPresentAndVisible(locator)) 
        {
            WebElement el = driver.findElement(locator);
            el.clear();
            el.sendKeys(value);
        }
    }
 
    private void scrollIntoView(By locator) 
    {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
    }
    
    public void waitForUserMenu() 
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userMenu));
    }
 
    
    public void waitForCartBadgeAtLeast(int expected) 
    {
        wait.until(d -> 
        {
            List<WebElement> badge = d.findElements(cartBadge);
            if (badge.isEmpty()) return false;
            String t = badge.get(0).getText().trim();
            return !t.isEmpty() && Integer.parseInt(t) >= expected;
        });
    }
 
    public void clickCartIcon() 
    {
        click(cartIcon);
    }
 
    public int getCartItemCount() 
    {
    	return wait.until(driver -> {
            List<WebElement> rows = driver.findElements(cartRows);

            if (!rows.isEmpty()) 
            {
                return rows.size();
            }

            List<WebElement> emptyMessages = driver.findElements(By.xpath("//*[contains(translate(normalize-space(.),"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'), 'empty cart')]"));

            for (WebElement element : emptyMessages) 
            {
                if (element.isDisplayed()) 
                {
                    return 0;
                }
            }

            return null;
        });
    }
 
    public List<String> getCartProductTitles() 
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartProductTitle));
        return driver.findElements(cartProductTitle).stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }
 
    public String getCartTotal() 
    {
        return text(cartTotal);
    }
 
    public void proceedFromCart() 
    {
        click(proceedFromCart);
    }
    
    public boolean isUserSignedIn() 
    {
        wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(signedInProceed),
                ExpectedConditions.visibilityOfElementLocated(guestTab)));
        return isPresentAndVisible(signedInProceed);
    }
    
    public void proceedAsSignedInUser() 
    {
        click(signedInProceed);
    }
    
    public void continueAsGuest(String email, String firstName, String lastName) 
    {
        click(guestTab);
        type(guestEmail, email);
        type(guestFirstName, firstName);
        type(guestLastName, lastName);
 
        scrollIntoView(guestSubmit);
        click(guestSubmit);            
        click(guestProceed);           
    }
    
    public boolean handleSignInStep(String guestEmailId, String guestFirst, String guestLast) 
    {
        if (isUserSignedIn()) 
        {
            proceedAsSignedInUser();
            return true;
        }
        continueAsGuest(guestEmailId, guestFirst, guestLast);
        return false;
    }
    
    public void fillBillingAddress(String country, String postal, String house,String streetName, String cityName, String stateName) 
    {
    
    	WebElement countryEl = wait.until(ExpectedConditions.elementToBeClickable(countrySelect));
        new Select(countryEl).selectByVisibleText(country);
 
        type(postalCode, postal);
        type(houseNumber, house);	 
        typeIfPresent(street, streetName);
        typeIfPresent(city, cityName);
        typeIfPresent(state, stateName);
    }
    
    public void proceedToPayment() 
    {
        click(proceedToPayment);
    }
    
    public void selectPaymentMethod(String visibleText) 
    {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(paymentMethod));
        new Select(dropdown).selectByVisibleText(visibleText);
    }
 
    public void enterBankTransferDetails(String bank, String accName, String accNumber) 
    {
        type(bankName, bank);
        type(accountName, accName);
        type(accountNumber, accNumber);
    }
    
    public void enterCreditCardDetails(String number, String expiry, String cvv, String holder) 
    {
        type(cardNumber, number);
        type(cardExpiry, expiry);      
        type(cardCvv, cvv);
        type(cardHolder, holder);
    }
    
    public String confirmPayment() 
    {
     wait.until(ExpectedConditions.elementToBeClickable(checkPaymentBtn)).click();
     wait.until(ExpectedConditions.elementToBeClickable(confirmBtn)).click();

    
    By confirmationLocator = By.cssSelector("#order-confirmation, [data-test='payment-success-message']");

    WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationLocator));

    return confirmation.getText().trim();
    }
    
    public String getPaymentSuccessMessage() 
    {
        return text(successMessage);
    }
 
    public String getOrderConfirmationText() 
    {
    	return wait.until(ExpectedConditions.visibilityOfElementLocated(orderConfirmation)).getText().trim();
    }
 
    public boolean isCartBadgeReset() 
    {
    	try 
    	{
            List<WebElement> badges = driver.findElements(cartBadge);

            if (badges.isEmpty()) 
            {
                return true;
            }

            WebElement badge = badges.get(0);
            if (!badge.isDisplayed()) 
            {
                return true;
            }

            String quantity = badge.getText().trim();
            return quantity.isEmpty() || quantity.equals("0");

        } catch (StaleElementReferenceException e) {
            
            return false;
        }
    }

    public boolean waitForCartBadgeReset() 
    {
    	 try {
    	        return wait.until(driver -> {
    	            List<WebElement> badges = driver.findElements(cartBadge);
    	            if (badges.isEmpty()) 
    	            {
    	                return true;
    	            }

    	            WebElement badge = badges.get(0);

    	            if (!badge.isDisplayed()) 
    	            {
    	                return true;
    	            }

    	            String count = badge.getText().trim();

    	            return count.isEmpty() || "0".equals(count);
    	        });
    	    } catch (TimeoutException e) 
    	 {
    	        return false;
    	    }
    }
    
    public boolean isCartEmpty() 
    {
    	System.out.println("URL while checking cart: " + driver.getCurrentUrl());

        List<WebElement> rows = driver.findElements(cartRows);

        for (WebElement row : rows) {
            if (row.isDisplayed()) {
                System.out.println("Visible cart row: " + row.getText());
            }
        }

        return rows.stream().noneMatch(WebElement::isDisplayed);
    }
    
    public String getCartBadgeText() 
    {
        List<WebElement> badge = driver.findElements(cartBadge);
        return badge.isEmpty() ? "(no badge)" : badge.get(0).getText().trim();
    }
    
    public void setCartQuantityToZero() 
    {
        WebElement quantity = wait.until(
            ExpectedConditions.visibilityOfElementLocated(cartQuantityInput));

        quantity.clear();
        quantity.sendKeys("0");
    }

    public String getCartQuantityValue() 
    {
        return wait.until( ExpectedConditions.visibilityOfElementLocated(cartQuantityInput)).getAttribute("value");
    }

    public boolean isCartValidationMessageDisplayed() 
    {
        return driver.findElements(cartValidationMessage).stream().anyMatch(element -> {
                try 
                {
                    return element.isDisplayed()&& !element.getText().trim().isEmpty();
                } catch (StaleElementReferenceException e) 
                {
                    return false;
                }
            });
    }

    public boolean isProceedFromCartEnabled() 
    {
        List<WebElement> buttons =driver.findElements(proceedFromCart);

        return !buttons.isEmpty()&& buttons.get(0).isDisplayed()&& buttons.get(0).isEnabled();
    }
   
   
}
