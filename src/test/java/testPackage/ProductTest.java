package testPackage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.ProductPage;

public class ProductTest extends BaseClass

{
	
	ProductPage product;
    String selectedProductName;

    @BeforeMethod
    public void selectProduct() 
    {
        product = new ProductPage(driver);

        if (selectedProductName == null) 
        {
            selectedProductName = product.selectRandomProduct();
        } 
        else 
        {
            product.selectProductByName(selectedProductName);
        }
    }
	
	@Test(priority = 1, description = "Verify product information details (Title, Price, Description)")
    public void testProductInformation() 
	{
        Assert.assertFalse(product.getProductName().isEmpty(), "Product name should not be empty.");
        Assert.assertFalse(product.getUnitPrice().isEmpty(), "Product price should not be empty.");
        Assert.assertFalse(product.getProductDescription().isEmpty(), "Product description should not be empty.");
    }
	
	@Test(priority = 2, description = "Verify incrementing and decrementing item quantity")
    public void testQuantityAdjustment() 
	{
        int initialQuantity = product.getQuantityValue();
        
        // Increment
        product.incrementQuantity();
        Assert.assertEquals(product.getQuantityValue(), initialQuantity + 1, "Quantity should increment by 1.");

        // Decrement
        product.decrementQuantity();
        Assert.assertEquals(product.getQuantityValue(), initialQuantity, "Quantity should decrement back.");
    }
	
	@Test(priority = 3, description = "Verify adding product to cart updates cart badge count")
    public void testAddToCart() 
	{
        product.clickAddToCart();

        Assert.assertTrue(product.isSuccessToastDisplayed(), "Success notification should appear.");
        Assert.assertTrue(product.getSuccessToastText().contains("Product added to shopping cart"), 
                "Toast message text mismatch!");
        Assert.assertTrue(product.getCartBadgeCount() > 0, "Cart badge should reflect added item quantity.");
    }
	
	  @Test(priority = 4, description = "Negative: Verify Category, Brand and Carbon rating is displayed")
	    public void verifyProductDetailsDisplayed() 
	  {

	        Assert.assertTrue(product.isCategoryDisplayed(),"Category name is not displayed");
            Assert.assertTrue(product.isBrandDisplayed(),"Brand name is not displayed");
            Assert.assertTrue(product.isCarbonRatingDisplayed(),"Carbon rating is not displayed");
	    }
	  
	  @Test(priority = 5, description = "Verify that setting product quantity to zero is rejected")
	  public void testZeroQuantityNegative() 
	  {

	      product.setQuantity(0);
	      int actualQuantity = product.getQuantityValue();

	      Assert.assertTrue( actualQuantity > 0,"Negative test failed: Product quantity should not be zero.");
	  }
	  
	  @Test( priority = 6,description = "Negative: Verify adding product to favourites without signing in displays a warning")
	  
			public void testAddToFavoritesWithoutLogin() 
	  {
        
		  product.clickAddToFavorites();

		    Assert.assertTrue(
		        product.isWarningMessageDisplayed(),"Warning message should appear for an unauthorized user.");

		    String warningMessage = product.getWarningMessageText();

		    Assert.assertTrue(
		        warningMessage.toLowerCase().contains("unauthorized")
		            && warningMessage.toLowerCase().contains("favorite list"),"Unexpected warning message: " + warningMessage);
			  
	  }
	}


