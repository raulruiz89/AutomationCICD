package raulruiz.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import raulruiz.AbstractComponents.AbstractComponent;

public class CheckoutPage extends AbstractComponent {

	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// driver.findElement(By.cssSelector("input[placeholder='Select
	// Country']")).sendKeys("mex");
	@FindBy(css = "input[placeholder='Select Country']")
	private WebElement desiredCountry;

	// driver.findElement(By.cssSelector("a[class*='action__submit']")).click();
	@FindBy(css = "a[class*='action__submit']")
	private WebElement submit;

	// List<WebElement> countries =
	// driver.findElements(By.cssSelector("span[class='ng-star-inserted']"));
	@FindBy(css = "span[class='ng-star-inserted']")
	private List<WebElement> availableCountries;
	
	By results = By.cssSelector(".ta-results");

	/*
	 * @FindBy(xpath = "(//button[contains(@class,'ta-item')])[2]") private
	 * WebElement selectCountry;
	 */

	//private By results = By.cssSelector(".ta-results");

	public void selectCountry(String countryName) {

		/*
		 * driver.findElement(By.cssSelector("input[placeholder='Select Country']")).
		 * sendKeys("mex");
		 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
		 * ".ta-results")));
		 * 
		 * List<WebElement> countries =
		 * driver.findElements(By.cssSelector("span[class='ng-star-inserted']"));
		 * countries.stream().filter(co ->
		 * co.getText().equalsIgnoreCase("mexico")).findFirst().orElse(null).click();
		 */

		desiredCountry.sendKeys(countryName);
		waitForElementToAppear(results);
		getDesiredCountry(countryName).click();

	}

	public WebElement getDesiredCountry(String countryName) {
		return availableCountries.stream().filter(co -> co.getText().equalsIgnoreCase(countryName)).findFirst()
				.orElse(null);
	}

	public ConfirmationPage submitOrder() {
		submit.click();
		return new ConfirmationPage(driver);

	}

}
