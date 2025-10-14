package raulruiz.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import raulruiz.PageObjects.CartPage;
import raulruiz.PageObjects.CheckoutPage;
import raulruiz.PageObjects.ConfirmationPage;
import raulruiz.PageObjects.OrderPage;
import raulruiz.PageObjects.ProductCatalogue;
import raulruiz.TestComponent.BaseTest;

public class SubmitOrderTest extends BaseTest {
	String productName = "ZARA COAT 3";

	@Test(dataProvider = "ExcelData", groups = { "Purchase" })
	public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {

		ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));

		productCatalogue.addProductToCart(input.get("product"));
		CartPage cartPage = productCatalogue.goToCartPage();

		boolean match = cartPage.VerifyProductDisplay(input.get("product"));
		Assert.assertTrue(match);
		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("mexico");
		ConfirmationPage confirmationPage = checkoutPage.submitOrder();

		String actualMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(actualMessage.equalsIgnoreCase("Thankyou for the order."));

	}

	// @Test(dependsOnMethods = { "submitOrder" })
	public void OrderHistoryTest() {
		// "ZARA COAT 3"
		ProductCatalogue productCatalogue = landingPage.loginApplication("rrulas10@hotmail.com", "Hqtzgl03521810");
		OrderPage ordersPage = productCatalogue.goToOrdersPage();
		Assert.assertTrue(ordersPage.VerifyOrderDisplay(productName));
	}

	@DataProvider(name="ExcelData")
	public Object[][] getDataExcel() throws IOException {
		String path = System.getProperty("user.dir") + "//src//test//java//raulruiz//data//DataReader.xlsx";
	    return getDataFromExcel(path);

	}

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "//src//test//java//raulruiz//data//PurchaseOrder.json");
		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}
}
