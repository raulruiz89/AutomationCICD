package stepDefinitions;

import java.io.IOException;

import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import raulruiz.PageObjects.CartPage;
import raulruiz.PageObjects.CheckoutPage;
import raulruiz.PageObjects.ConfirmationPage;
import raulruiz.PageObjects.LandingPage;
import raulruiz.PageObjects.ProductCatalogue;
import raulruiz.TestComponent.BaseTest;

public class StepDefinitionImpl extends BaseTest {

	public LandingPage landingPage;
	public ProductCatalogue productCatalogue;
	public CartPage cartPage;
	public CheckoutPage checkoutPage;
	public ConfirmationPage confirmationPage;

	@Given("I landed on Ecommerce Page")
	public void EcommercePage() throws IOException {
		landingPage = launchApplication();
	}

	@Given("^Logged in with username (.+) and password (.+)$")
	public void UserAndPassword(String username, String password) {
		productCatalogue = landingPage.loginApplication(username, password);
	}

	@When("^I add product (.+) to Cart$")
	public void ProductToCart(String product) throws InterruptedException {
		productCatalogue.addProductToCart(product);

	}

	@When("^Checkout (.+) and submit the order$")
	public void CheckoutAndSubmit(String product) {
		cartPage = productCatalogue.goToCartPage();
		boolean match = cartPage.VerifyProductDisplay(product);
		Assert.assertTrue(match);
		checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("mexico");
		confirmationPage = checkoutPage.submitOrder();
	}

	@Then("{string} message is displayed on ConfirmationPage")
	public void messageDisplayed(String message) {
		String actualMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(actualMessage.equalsIgnoreCase(message));
		driver.close();
	}
	
	@Then("{string} message is displayed")
	public void messageDisplayedErrorValidations(String message) {
		Assert.assertEquals(message, landingPage.getErrorMessage());
		driver.close();
	}
}
