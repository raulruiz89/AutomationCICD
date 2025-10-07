package raulruiz.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import raulruiz.AbstractComponents.AbstractComponent;

public class ProductCatalogue extends AbstractComponent {

	WebDriver driver;

	public ProductCatalogue(WebDriver driver) {
		super(driver);
		// initialization
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	// List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
	@FindBy(css = ".mb-3")
	List<WebElement> products;

	// driver.findElement(By.cssSelector(".ng-animating")
	@FindBy(css = ".ng-animating")
	WebElement spinner;

	By productsBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage = By.cssSelector("#toast-container");

	public List<WebElement> getProductList() {
		waitForElementToAppear(productsBy);
		return products;
	}

	public WebElement getProductByName(String productName) {

		/*WebElement prod = getProductList().stream()
				.filter(p -> p.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
		return prod;*/
		
		return getProductList().stream()
				.filter(p -> p.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
	}

	public void addProductToCart(String productName) throws InterruptedException {

		// prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();
		WebElement prod = getProductByName(productName);
		prod.findElement(addToCart).click();

		// Using # on CSSSelector is for ID
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
		waitForElementToAppear(toastMessage);

		// wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		waitForElementToDisappear(spinner);

	}

}
