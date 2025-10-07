package raulruiz.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import raulruiz.PageObjects.CartPage;
import raulruiz.PageObjects.ProductCatalogue;
import raulruiz.TestComponent.BaseTest;
import raulruiz.TestComponent.Retry;

public class ErrorValidationsTest extends BaseTest {

	@Test(groups = { "ErrorHandling" }, retryAnalyzer = Retry.class)
	public void LoginErrorValidation() throws IOException, InterruptedException {
		landingPage.loginApplication("rrolas10@hotmail.com", "Hqtzgl03521810");
		Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
	}

	@Test
	public void ProductErrorValidation() throws IOException, InterruptedException {
		String productName = "ZARA COAT 3";
		ProductCatalogue productCatalogue = landingPage.loginApplication("baloo@xyz.com", "Hqtzgl03521810");

		productCatalogue.addProductToCart(productName);
		CartPage cartPage = productCatalogue.goToCartPage();

		boolean match = cartPage.VerifyProductDisplay("ZARA COAT 33");
		Assert.assertFalse(match);
	}

}
