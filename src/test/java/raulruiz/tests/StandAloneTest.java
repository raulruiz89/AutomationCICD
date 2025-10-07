package raulruiz.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class StandAloneTest {

	public static void main(String[] args) throws InterruptedException {
		String productName = "ZARA COAT 3";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://rahulshettyacademy.com/client");

		driver.findElement(By.id("userEmail")).sendKeys("rrulas10@hotmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Hqtzgl03521810");
		driver.findElement(By.id("login")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".mb-3"))));

		//Catching ALL items to add to cart
		List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
		
		//Use of streams to select a desired item to buy
		WebElement product = products.stream()
				.filter(p -> p.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);

		//Click on "Add to Cart" button
		product.findElement(By.cssSelector(".card-body button:last-of-type")).click();

		// Confirm that products are successfully added to cart by waiting until "Product added to cart" is displayed
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
		
		// ng-animating
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		
		driver.findElement(By.cssSelector("[routerlink*='cart']")).click();
		
		// Check whether items are added to cart
		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));
		
		Boolean isProductMatch = cartProducts.stream().anyMatch(cp -> cp.getText().equalsIgnoreCase(productName));
		
		Assert.assertTrue(isProductMatch);
		
		// Go to checkout page
		driver.findElement(By.cssSelector(".totalRow button")).click();
		
		
		
		//Display countries by sending just 3 characters and wait till the list is displayed
		driver.findElement(By.cssSelector("input[placeholder='Select Country']")).sendKeys("mex");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));
		
		//Catch all countries into a list and use stream to select the desired one
		List<WebElement> countries = driver.findElements(By.cssSelector("span[class='ng-star-inserted']"));
		
		countries.stream().filter(co -> co.getText().equalsIgnoreCase("mexico")).findFirst().orElse(null).click();
		
		//Click on "Place Order" button
		driver.findElement(By.cssSelector("a[class*='action__submit']")).click();
		
		

		//Verify "thank you for the order" is displayed
		String actualMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
		Assert.assertTrue(actualMessage.equalsIgnoreCase("Thankyou for the order."));

	}

}
