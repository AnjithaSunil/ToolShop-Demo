package pagePackage;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage 
{
	private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    
    By brandlogo = By.className("navbar-brand");
    By home = By.xpath("//a[text()='Home']");
    By category = By.cssSelector("a[data-test='nav-categories']");
    By contact = By.linkText("Contact");
    By signin = By.linkText("Sign in");
    By language = By.id("language");
    By search = By.id("search-query");
    By searchbutton = By.xpath("//button[@data-test='search-submit']");
    By sortdropdown = By.xpath("//select[@data-test='sort']");
    By minhandler = By.xpath("//span[contains(@class,'ngx-slider-pointer-min')]");
    By maxhandler = By.xpath("//span[contains(@class,'ngx-slider-pointer-max')]");
    By categoryCheckboxes=By.name("category_id");
    By brandCheckboxes=By.name("brand_id");
    By sustainability = By.name("eco_friendly");
    By productgrid = By.className("card-img-top");
    By producttitle= By.className("card-title");
    By nextpage = By.xpath("//button[@data-test='pagination-next']");
    By footerlinks=By.xpath("//a[contains(@class,'ptt-explore-link')]");
    By noResultsMessage = By.xpath("//div[@data-test='no-results']");
    
    public HomePage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);    
    }
    
    public void clickBrandLogo() 
    {
        wait.until(ExpectedConditions.elementToBeClickable(brandlogo)).click();
    }

    public void selectLanguage(String langCode) 
    {
    	WebElement languageButton = wait.until(
                ExpectedConditions.elementToBeClickable(language));

        languageButton.click();

        
    }
    
    public void executeSearch(String keyword) 
    {
    	 WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(search));

    	    searchField.clear();
    	    searchField.sendKeys(keyword);

    	    wait.until(ExpectedConditions.elementToBeClickable(searchbutton)).click();
    }
    
    public void adjustPriceRange(int xOffsetMin, int xOffsetMax) 
    {
        if (xOffsetMin != 0) 
        {
        	WebElement minhandlerbar=driver.findElement(minhandler);
            actions.dragAndDropBy(minhandlerbar, xOffsetMin, 0).perform();
        }
        if (xOffsetMax != 0) 
        {
        	WebElement maxhandlerbar=driver.findElement(maxhandler);
            actions.dragAndDropBy(maxhandlerbar, xOffsetMax, 0).perform();
        }
    }
    
    public void filterByCategoryName(String categoryName) 
    {
    	List<WebElement> checkboxes = driver.findElements(categoryCheckboxes);
        for (WebElement checkbox : checkboxes) 
        {
            if (checkbox.getAttribute("value").equalsIgnoreCase(categoryName) || checkbox.getText().contains(categoryName)) 
            {
                if (!checkbox.isSelected()) 
                {
                    checkbox.click();
                }
                break;
            }
        }
    }
    
    public void selectSortOption(String visibleTextOrValue) 
    {
    	Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(sortdropdown)));

        select.selectByVisibleText(visibleTextOrValue);
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortdropdown));
    }
    
    public int getProductCount()
    {
        for (int attempt = 0; attempt < 3; attempt++)
        {
            try
            {
                return wait.until(driver ->
                {
                    List<WebElement> products =
                            driver.findElements(productgrid);

                    int count = 0;

                    for (WebElement product : products)
                    {
                        try
                        {
                            if (product.isDisplayed())
                            {
                                count++;
                            }
                        }
                        catch (org.openqa.selenium.StaleElementReferenceException e)
                        {
                            return null;
                        }
                    }

                    // Wait until at least one product is displayed
                    return count > 0 ? count : null;
                });
            }
            catch (org.openqa.selenium.StaleElementReferenceException e)
            {
                // Retry if product elements become stale
            }
            catch (org.openqa.selenium.TimeoutException e)
            {
                if (attempt == 2)
                {
                    throw new org.openqa.selenium.TimeoutException(
                            "No visible product images found after retries", e);
                }
            }
        }

        throw new org.openqa.selenium.TimeoutException(
                "Product images remained stale after retries");
    }
    
    public boolean isProductDisplayed(String titleKeyword) 
    {
    	    for (int attempt = 0; attempt < 3; attempt++)
    	    {
    	        try
    	        {
    	            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(producttitle));

    	            List<WebElement> titles = driver.findElements(producttitle);

    	            for (WebElement title : titles)
    	            {
    	                if (title.getText().toLowerCase().contains(titleKeyword.toLowerCase())) 
    	                {
    	                    return true;
    	                }
    	            }

    	            return false;

    	        } catch (org.openqa.selenium.StaleElementReferenceException e) {
    	            
    	        }
    	    }

    	    return false;
    	}
    
    
    public void navigateToNextPage() 
    {
    	WebElement nextpagebttn = driver.findElement(nextpage);
        if (nextpagebttn.isDisplayed() && nextpagebttn.isEnabled()) 
        {
            actions.moveToElement(nextpagebttn).click().perform();
        }
    }
    
    public int getFooterLinksCount() {
    	
        return driver.findElements(footerlinks).size();
    }
    
    public boolean isNoResultsMessageDisplayed()
    {
        try
        {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).isDisplayed();
        }
        catch (org.openqa.selenium.TimeoutException e)
        {
            return false;
        }
    }
    
    public int getDisplayedProductCount()
    {
        return driver.findElements(productgrid).size();
    }
    
    public int getMinPrice()
    {
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(minhandler))
                                    .getAttribute("aria-valuenow"));
    }

    public int getMaxPrice()
    {
        return Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(maxhandler))
                                    .getAttribute("aria-valuenow"));
    }

    // Drag the max handle left, past the min handle
    public void dragMaxHandleBeyondMin()
    {
        WebElement min = wait.until(ExpectedConditions.visibilityOfElementLocated(minhandler));
        WebElement max = wait.until(ExpectedConditions.visibilityOfElementLocated(maxhandler));
        int offset = min.getLocation().getX() - max.getLocation().getX() - 15;
        new Actions(driver).dragAndDropBy(max, offset, 0).perform();
    }

    // Drag the min handle right, past the max handle
    public void dragMinHandleBeyondMax()
    {
        WebElement min = wait.until(ExpectedConditions.visibilityOfElementLocated(minhandler));
        WebElement max = wait.until(ExpectedConditions.visibilityOfElementLocated(maxhandler));
        int offset = max.getLocation().getX() - min.getLocation().getX() + 15;
        new Actions(driver).dragAndDropBy(min, offset, 0).perform();
    }
    
    public boolean isApplicationLoaded() 
    {
    	String Base_URL = "https://practicesoftwaretesting.com/";
        try {
            return driver.getCurrentUrl().startsWith(Base_URL)
                    && !driver.getTitle().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
   
}
