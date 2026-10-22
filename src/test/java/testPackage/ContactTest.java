package testPackage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import basePackage.BaseClass;
import pagePackage.ContactPage;

public class ContactTest extends BaseClass
{

  ContactPage contactpage;
  
  @BeforeMethod
  public void initializePage() 
  {
	  contactpage= new ContactPage(driver);
	  contactpage.contactpageload();
  }
  
  @Test(priority = 1, description = "Open contact page") 
  public void contactpageopen() 
  { 
	  Assert.assertTrue(contactpage.isContactFormDisplayed(), "Contact form should be displayed"); 
  }
  
  @Test(priority = 2, description = "Submit contact form with all valid fields")
  public void testSuccessfulFormSubmission() {
      contactpage.enterFirstName("Alex");
      contactpage.enterLastName("Morgan");
      contactpage.enterEmail("alex.morgan@example.com");
      contactpage.selectSubject("Customer service");
      contactpage.enterMessage("Requesting information regarding product warranty options.");
      contactpage.clickSubmit();

      Assert.assertTrue(contactpage.isSuccessAlertDisplayed(), "Success banner should appear.");
      Assert.assertTrue(contactpage.getSuccessAlertText().contains("Thanks for your message"), "Unexpected success text!");
  }
  
  @Test(priority = 3, description = "Verify email validation message for invalid input format")
  public void testInvalidEmailValidation() {
      contactpage.enterFirstName("Sam");
      contactpage.enterLastName("Taylor");
      contactpage.enterEmail("sam@@gmail.com");
      contactpage.enterMessage("Testing email validation error detection.");
      contactpage.clickSubmit();

      Assert.assertEquals(contactpage.getEmailErrorText(),"Email format is invalid");
  }
  
  @Test(priority = 4, description = "Verify error alert message when entering less than the minimum required characters in message field")
  public void testMinimumCharacterAlertOnMessageField() {
      
      contactpage.enterFirstName("Jane");
      contactpage.enterLastName("Doe");
      contactpage.enterEmail("jane.doe@example.com");
      contactpage.selectSubject("Customer service");
      contactpage.enterMessage("Too short"); // Only 9 characters

      contactpage.clickSubmit();

      // Verify error alert presence and message text
      Assert.assertTrue( contactpage.isMessageErrorDisplayed(), 
    		  "Message error alert should be visible when input is below the minimum length." ); 
      String actualErrorText = contactpage.getMessageErrorText(); 
      String expectedErrorSubstring = "Message must be minimal 50 characters"; 
      Assert.assertTrue( actualErrorText.contains(expectedErrorSubstring), 
    		  "Alert text mismatch! Expected to contain: '" + expectedErrorSubstring + "' but found: '" + actualErrorText + "'" );
  }
	

}
