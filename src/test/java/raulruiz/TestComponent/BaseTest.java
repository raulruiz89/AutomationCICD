package raulruiz.TestComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import raulruiz.PageObjects.LandingPage;

public class BaseTest {

	public WebDriver driver;
	public LandingPage landingPage;

	public WebDriver initializeDriver() throws IOException {
		// properties class

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//raulruiz//resources//GlobalData.properties");
		prop.load(fis);

		String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
				: prop.getProperty("browser");

		// String browserName = prop.getProperty("browser");

		if (browserName.contains("chrome")) {
			WebDriverManager.chromedriver().setup();

			ChromeOptions options = new ChromeOptions();
			if (browserName.contains("headless")) {
				options.addArguments("headless");
			}

			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1440, 900));// full screen

		} else if (browserName.equalsIgnoreCase("firefox")) {

			// System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver.exe");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

		} else if (browserName.equalsIgnoreCase("edge")) {

			// System.setProperty("webdriver.edge.driver", "C:\\drivers\\msedgedriver.exe");
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {

		// read json to string
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		// String to HashMap using Jackson Databind
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});

		return data;

	}

	public Object[][] getDataFromExcel(String filePath) throws IOException {

		DataFormatter formatter = new DataFormatter();

		try (FileInputStream fis = new FileInputStream(filePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

			XSSFSheet sheet = workbook.getSheetAt(0);

			int numberOfRows = sheet.getPhysicalNumberOfRows();
			if (numberOfRows < 2) {
				return new Object[0][1]; // no hay filas de datos
			}

			XSSFRow headerRow = sheet.getRow(0);
			int colCount = headerRow.getLastCellNum();

			// Leer encabezados
			String[] headers = new String[colCount];
			for (int j = 0; j < colCount; j++) {
				XSSFCell cell = headerRow.getCell(j);
				headers[j] = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
			}

			// Cada fila de datos -> 1 HashMap como único parámetro del test
			Object[][] data = new Object[numberOfRows - 1][1];

			for (int i = 0; i < numberOfRows - 1; i++) {
				XSSFRow row = sheet.getRow(i + 1);
				java.util.HashMap<String, String> map = new java.util.LinkedHashMap<>();

				for (int j = 0; j < colCount; j++) {
					String key = (headers[j] == null) ? "" : headers[j].trim();
					if (key.isEmpty())
						continue; // columna sin encabezado: se omite

					XSSFCell cell = (row == null) ? null : row.getCell(j);
					String value = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
					map.put(key, value);
				}

				data[i][0] = map; // <- único argumento para submitOrder(HashMap<String,String> input)
			}

			return data;
		}
	}

	///////////////////////////////////////////////////////////////////////////

//		DataFormatter formatter = new DataFormatter();
//
//		FileInputStream fis = new FileInputStream(filePath);
//		XSSFWorkbook workbook = new XSSFWorkbook(fis);
//
//		XSSFSheet sheet = workbook.getSheetAt(0);
//		int numberOfRows = sheet.getPhysicalNumberOfRows();
//		XSSFRow row = sheet.getRow(0);
//		int colCount = row.getLastCellNum();
//
//		Object data[][] = new Object[numberOfRows - 1][colCount];
//
//		for (int i = 0; i < numberOfRows - 1; i++) {
//			row = sheet.getRow(i + 1);
//			for (int j = 0; j < colCount; j++) {
//				XSSFCell cell = row.getCell(j);
//				data[i][j] = formatter.formatCellValue(cell);
//			}
//		}
//		
//		return data;

	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";

	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {

		driver = initializeDriver();
		landingPage = new LandingPage(driver);
		landingPage.goTo();
		return landingPage;

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.close();
	}
}
