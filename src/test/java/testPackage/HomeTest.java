package testPackage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.HomePage;

public class HomeTest extends BaseClass
{
	
	HomePage homePage;

    @BeforeMethod
    public void initializepage()
    {
    	homePage=new HomePage(driver);
    }
    
    @Test(priority = 0, description = "Verify application launch in Chrome and Firefox")
    public void testApplicationLaunchAcrossBrowsers() 
    {

        WebDriver browser = null;

        try {
            // Verify Chrome
            browser = new ChromeDriver();
            browser.manage().window().maximize();
            browser.get(Base_URL);

            HomePage chromeHome = new HomePage(browser);

            Assert.assertTrue(chromeHome.isApplicationLoaded(),
                    "Application failed to load in Chrome");

            System.out.println("Application loaded successfully in Chrome");

            browser.quit();
            browser = null;

            // Verify Firefox
            browser = new FirefoxDriver();
            browser.manage().window().maximize();
            browser.get(Base_URL);

            HomePage firefoxHome = new HomePage(browser);

            Assert.assertTrue(firefoxHome.isApplicationLoaded(),
                    "Application failed to load in Firefox");

            System.out.println("Application loaded successfully in Firefox");

        } finally {
            if (browser != null) {
                browser.quit();
            }
        }
    }

    @Test(priority = 1, description = "Verify Header Routing, & Language Selection")
    public void testGlobalHeaderControls() 
    {
        
        homePage.selectLanguage("de"); 
        
        homePage.clickBrandLogo();
        Assert.assertEquals(driver.getCurrentUrl(), Base_URL);
    }

    @Test(priority = 2, description = "Verify Sidebar Search and Filter Systems")
    public void testSearchAndFiltering() 
    {
        // Keyword Search
        homePage.executeSearch("Pliers");
        Assert.assertTrue(homePage.getProductCount() > 0, "Products should be displayed for query 'Pliers'");
        Assert.assertTrue(homePage.isProductDisplayed("Pliers"), "Grid items should contain 'Pliers'");

        homePage.adjustPriceRange(20, -30);
    }

    @Test(priority = 3, description = "Verify Sorting Controls Options")
    public void testSortingControls() 
    {
        // Sort by Name (A-Z)
        homePage.selectSortOption("Name (A - Z)");
        Assert.assertTrue(homePage.getProductCount() > 0);

        // Sort by Price (Low - High)
        homePage.selectSortOption("Price (Low - High)");
        Assert.assertTrue(homePage.getProductCount() > 0);
    }

    @Test(priority = 4, description = "Verify Product Grid & Pagination")
    public void testGridAndPagination() 
    {
    	driver.get(Base_URL);

        int initialCount = homePage.getProductCount();

        Assert.assertTrue(initialCount > 0,
                "Initial grid should render product cards. Found: " + initialCount);

        System.out.println("Initial product count: " + initialCount);

        // Navigate to the next page
        homePage.navigateToNextPage();

        int nextPageCount = homePage.getProductCount();

        Assert.assertTrue(nextPageCount > 0,
                "Product grid should display products after pagination");

        System.out.println("Products after pagination: " + nextPageCount);
    }

    @Test(priority = 5, description = "Verify Footer Informational Links Presence")
    public void testFooterLinks() {
        int linksCount = homePage.getFooterLinksCount();
        Assert.assertTrue(linksCount > 0, "Footer should render navigational links");
    }
    
    @Test(priority = 6, description = "Negative: Search with a keyword that matches no product")
    public void testSearchWithNonExistentKeyword()
    {
        driver.get(Base_URL);   

        homePage.executeSearch("xyzabc123");

        Assert.assertTrue(homePage.isNoResultsMessageDisplayed(),
                "A 'no products found' message should be shown for an unknown keyword");
        Assert.assertEquals(homePage.getDisplayedProductCount(), 0,
                "No product cards should be displayed for an unknown keyword");
    }

    @Test(priority = 7, description = "Negative: Search with special/script characters is handled safely")
    public void testSearchWithSpecialCharacters()
    {
        driver.get(Base_URL);

        homePage.executeSearch("<script>alert(1)</script>");

        Assert.assertTrue(homePage.isNoResultsMessageDisplayed(),
                "Special characters should return no results, not an error");
        Assert.assertEquals(homePage.getDisplayedProductCount(), 0);

        
        try
        {
            driver.switchTo().alert();
            Assert.fail("An unexpected JavaScript alert appeared, so the input was not sanitised");
        }
        catch (org.openqa.selenium.NoAlertPresentException expected)
        {
            
        }
    }
    
    @Test(priority = 8, description = "Negative: Price slider handles must not cross each other")
    public void testPriceSliderHandlesCannotCross()
    {
        driver.get(Base_URL);

        // Case 1: drag MAX handle towards (and past) MIN
        homePage.dragMaxHandleBeyondMin();
        int min = homePage.getMinPrice();
        int max = homePage.getMaxPrice();
        Assert.assertTrue(max >= min,
                "ERROR: Max handle crossed below min handle (min=" + min + ", max=" + max + ")");

        // Reset the slider, then Case 2: drag MIN handle towards (and past) MAX
        driver.navigate().refresh();

        homePage.dragMinHandleBeyondMax();
        min = homePage.getMinPrice();
        max = homePage.getMaxPrice();
        Assert.assertTrue(min <= max,
                "ERROR: Min handle crossed above max handle (min=" + min + ", max=" + max + ")");
    }


}
