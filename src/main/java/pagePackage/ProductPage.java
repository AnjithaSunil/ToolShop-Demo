package pagePackage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage 

{
	
	private WebDriver driver;
    private WebDriverWait wait;
    
    By prod_title=By.xpath("//h5[@data-test='product-name']");
    By detail_title = By.cssSelector("h1");
    By prod_price=By.xpath("//span[@aria-label='unit-price']");
    By prod_descr=By.id("description");
    By quant_input=By.id("quantity-input");
    By decrease_quant=By.xpath("//button[@data-test='decrease-quantity']");
    By increase_quant=By.xpath("//button[@data-test='increase-quantity']");
    By cart=By.cssSelector("[data-test='add-to-cart']");
    By cart_badge=By.xpath("//span[@data-test='cart-quantity']");
    By add_fav=By.id("btn-add-to-favorites");
    By compare=By.xpath("//button[@data-test='add-to-compare']");
    By category=By.xpath("//span[@aria-label='category']");
    By brand=By.xpath("//span[@aria-label='brand']");
    By carbon_rating=By.xpath("//*[@data-test='co2-rating-badge']");
    By successalert = By.cssSelector(".toast-success, .alert-success, [role='alert']");
    By warningAlert = By.cssSelector(".toast-warning, .alert-warning, .toast-error, .alert-danger");
    
    public ProductPage(WebDriver driver)
    {
    	  this.driver = driver;
	      this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	      
    }
    
    public String selectRandomProduct() {

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(prod_title));

        List<WebElement> products = driver.findElements(prod_title);

        List<WebElement> availableProducts = new ArrayList<>();

        for (WebElement product : products) {

            WebElement productCard = product.findElement(By.xpath("./ancestor::*[.//*[@data-test='product-name']][1]"));

            String productDetails = productCard.getText().toLowerCase();

            if (!productDetails.contains("out of stock")) {
                availableProducts.add(product);
            }
        }

        if (availableProducts.isEmpty()) {
            throw new RuntimeException("No in-stock products found in the product grid");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(availableProducts.size());

        WebElement selectedProduct = availableProducts.get(randomIndex);
        String productName = selectedProduct.getText().trim();

        wait.until(ExpectedConditions.elementToBeClickable(selectedProduct)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(detail_title));

        return productName;
    }
    
    public void selectProductByName(String name) 
    {
        By productByName = By.xpath("//*[@data-test='product-name' and normalize-space()='" + name + "']");
        wait.until(ExpectedConditions.elementToBeClickable(productByName)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(detail_title));
    }
    public String getProductName() 
    {
    	 return wait.until(ExpectedConditions.visibilityOfElementLocated(detail_title)).getText().trim();
    }

    public String getUnitPrice() 
    {
    	WebElement unitprice=driver.findElement(prod_price);
        return wait.until(ExpectedConditions.visibilityOf(unitprice)).getText().trim();
    }

    public String getProductDescription() 
    {
    	WebElement productdescription=driver.findElement(prod_descr);
        return wait.until(ExpectedConditions.visibilityOf(productdescription)).getText().trim();
    }

    public int getQuantityValue() 
    {
    	WebElement quantityinput=driver.findElement(quant_input);
        return Integer.parseInt(quantityinput.getAttribute("value"));
    }
    
    public void incrementQuantity() 
    {
    	WebElement increasequantityBtn=driver.findElement(increase_quant);
        wait.until(ExpectedConditions.elementToBeClickable(increasequantityBtn)).click();
    }

    public void decrementQuantity() 
    {
    	WebElement decreasequantityBtn=driver.findElement(decrease_quant);
        wait.until(ExpectedConditions.elementToBeClickable(decreasequantityBtn)).click();
    }

    public void setQuantity(int quantity) 
    {
    	WebElement quantityinput=driver.findElement(quant_input);
        wait.until(ExpectedConditions.visibilityOf(quantityinput)).clear();
        quantityinput.sendKeys(String.valueOf(quantity));
    }
    
    public void clickAddToCart() 
    {
    	WebElement addtocart=driver.findElement(cart);
        wait.until(ExpectedConditions.elementToBeClickable(addtocart)).click();
    }
    
    public void clickAddToFavorites() 
    {
    	WebElement addtofavourite=driver.findElement(add_fav);
        wait.until(ExpectedConditions.elementToBeClickable(addtofavourite)).click();
    }

    public boolean isSuccessToastDisplayed() 
    {
    	return wait.until(ExpectedConditions.visibilityOfElementLocated(successalert)).isDisplayed();
    }

    public String getSuccessToastText() 
    {
    	return wait.until(ExpectedConditions.visibilityOfElementLocated(successalert)).getText().trim();
    }

    public boolean isWarningMessageDisplayed() 
    {
        return wait.until( ExpectedConditions.visibilityOfElementLocated(warningAlert)).isDisplayed();
    }

    public String getWarningMessageText() 
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(warningAlert)).getText().trim();
    }
    
    public int getCartBadgeCount() 
    {
        WebElement cartbadge=driver.findElement(cart_badge);
        String count= wait.until(ExpectedConditions.visibilityOf(cartbadge)).getText().trim();
        return Integer.parseInt(count);
    }
    
    public boolean isCategoryDisplayed() 
    {
        return driver.findElement(category).isDisplayed();
    }

    public boolean isBrandDisplayed() 
    {
        return driver.findElement(brand).isDisplayed();
    }

    public boolean isCarbonRatingDisplayed() 
    {
        return driver.findElement(carbon_rating).isDisplayed();
    }

}
