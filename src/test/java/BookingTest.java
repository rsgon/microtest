import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.BookingPage;
import pages.ResultPage;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

@Epic("Test task")
@Feature("Testng crossbrowser testing")
public class BookingTest {

	public String CURRENCY_EURO = "euro";
	public String LANGUAGE_ENGLISH_US = "EnglishUS";
	public String DESTINATION_KYIV_UKRAINE = "Kyiv, Ukraine";
	public WebDriver webDriver;
	public BookingPage bookingPage;
	public ResultPage resultPage;

	@BeforeTest
	@Parameters("browser")
	public void setUp(String browser) throws Exception {
		if(browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			webDriver = new ChromeDriver();
		} else if(browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			webDriver = new FirefoxDriver();
		} else {
			//If no browser passed throw exception
			throw new Exception("Browser is not correct");
		}
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	@AfterTest
	public void tearDown() {
		if (webDriver != null) {
			webDriver.quit();
		}
	}

	@Test
	@Story("Booking apartments")
	@Description("Test Description: Available apartments search at least one result with review mark higher 8.1 and price more than 205 EUR.")
	public void searchAvailablePlacesTest() {
		bookingPage = new BookingPage(webDriver);
		bookingPage.open();
		bookingPage.clickLanguageSelector().chooseLanguage(LANGUAGE_ENGLISH_US);
		bookingPage.clickCurrencySelector().chooseCurrency(CURRENCY_EURO);
		bookingPage.inputDestination(DESTINATION_KYIV_UKRAINE);
		bookingPage.clickCkeckIn().chooseDays();
		bookingPage.setQuantityOfGuestsAndRooms(1, 1, 5, 2);
		resultPage = bookingPage.setTravelingForWorkCheckbox().clickSearch();
		assertTrue(resultPage.isResultHasAcceptableConditions());

	}

}
