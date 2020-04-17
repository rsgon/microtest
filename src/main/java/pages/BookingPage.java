package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BookingPage {

	public WebDriver webDriver;

	public String BOOKING_URL = "https://www.booking.com";

	@FindBy(xpath = "//input[@id='ss']")
	public WebElement destinationField;

	@FindBy(xpath = "//span[contains(text(),'Search')]")
	public WebElement searchButton;

	@FindBy(xpath = "//li[@data-id='currency_selector']")
	public WebElement currencySelector;

	@FindBy(xpath = "//li[@data-id='language_selector']")
	public WebElement languageSelector;

	@FindBy(xpath = "//div[@id='currency_dropdown_all']//li[@data-lang='EUR']")
	public WebElement euro;

	@FindBy(xpath = "//li[@data-lang='en-us']")
	public WebElement languageEnglishUS;

	@FindBy(xpath = "//div[@class='xp__dates xp__group']")
	public WebElement checkInInfo;

	@FindBy(xpath = "//div[@class='bui-calendar__wrapper'][1]//span/span")
	public List<WebElement> daysInCurrentMonth;

	@FindBy(xpath = "//div[@class='bui-calendar__wrapper'][2]//span/span")
	public List<WebElement> daysInNextMonth;

	@FindBy(xpath = "//label[@id='xp__guests__toggle']")
	public WebElement guests;

	@FindBy(xpath = "//div[contains(@class,'adults')]//span[@data-bui-ref]")
	public WebElement currentAdults;

	@FindBy(xpath = "//div[contains(@class,'adults')]//button[contains(@class,'subtract-button')]/span")
	public WebElement decreaseAdults;

	@FindBy(xpath = "//div[contains(@class,'adults')]//button[contains(@class,'add-button')]/span")
	public WebElement increaseAdults;

	@FindBy(xpath = "//div[contains(@class,'children')]//span[@data-bui-ref]")
	public WebElement currentChilds;

	@FindBy(xpath = "//div[contains(@class,'children')]//button[contains(@class,'subtract-button')]")
	public WebElement decreaseChilds;

	@FindBy(xpath = "//div[contains(@class,'children')]//button[contains(@class,'add-button')]")
	public WebElement increaseChilds;

	@FindBy(xpath = "//div[contains(@class,'rooms')]//span[@data-bui-ref]")
	public WebElement currentRooms;

	@FindBy(xpath = "//div[contains(@class,'rooms')]//button[contains(@class,'subtract-button')]")
	public WebElement decreaseRooms;

	@FindBy(xpath = "//div[contains(@class,'rooms')]//button[contains(@class,'add-button')]")
	public WebElement increaseRooms;

	@FindBy(xpath = "//select[@name='age']")
	public WebElement childAge;

	@FindBy(xpath = "//label[contains(text(),\"I'm traveling for work\")]")
	public WebElement checkboxTravelForWork;

	public BookingPage(WebDriver driver) {
		this.webDriver = driver;
		PageFactory.initElements(driver, this);
	}

	@Step("Open site \"https://www.booking.com\"")
	public BookingPage open() {
		webDriver.get(BOOKING_URL);
		return this;
	}

	@Step("Click on currency icon")
	public BookingPage clickCurrencySelector() {
		currencySelector.click();
		return this;
	}

	@Step("Click on language icon")
	public BookingPage clickLanguageSelector() {
		languageSelector.click();
		return this;
	}

	@Step("Choose currency [0]")
	public BookingPage chooseCurrency(String currencyName) {
		WebElement element = null;
		switch (currencyName) {
			case "euro":
				element = euro;
				break;
			default:
				break;
		}
		element.click();
		return this;
	}

	@Step("Choose language [0]")
	public BookingPage chooseLanguage(String language) {
		WebElement element = null;
		switch (language) {
			case "EnglishUS":
				element = languageEnglishUS;
				break;
			default:
				break;
		}
		element.click();
		return this;
	}

	@Step("Input destination pint [0]")
	public void inputDestination(String string) {
		WebElement element = destinationField;
		element.sendKeys(string);
	}

	@Step("Open check-in check-out calendar popup")
	public BookingPage clickCkeckIn() {
		WebElement element = checkInInfo;
		element.click();
		return this;
	}

	@Step("Choose days in calendar popup")
	public BookingPage chooseDays() {
		daysInCurrentMonth.stream().skip(daysInCurrentMonth.size() - 1).findAny().orElse(null).click();
		daysInNextMonth.stream().findFirst().orElse(null).click();
		return this;
	}

	@Step("Set quantity of adults, children, rooms and children age")
	public BookingPage setQuantityOfGuestsAndRooms(int expectedAdults, int expectedChildrens, int childsAge, int expectedRooms) {
		guests.click();
		setExpectedValue(expectedAdults, currentAdults, decreaseAdults, increaseAdults);
		setExpectedValue(expectedChildrens, currentChilds, decreaseChilds, increaseChilds);
		setExpectedValue(expectedRooms, currentRooms, decreaseRooms, increaseRooms);
		Select selectAge = new Select(childAge);
		selectAge.selectByIndex(childsAge + 1);
		guests.click();
		return this;
	}

	private void setExpectedValue(int expectedValue, WebElement currentValue, WebElement distinct, WebElement add) {
		int value = Integer.parseInt(currentValue.getText());
		if (value != expectedValue) {
			if (value < expectedValue) {
				do {
					add.click();
					value = Integer.parseInt(currentValue.getText());
					currentValue.click();
				} while (value < expectedValue);

			} else if (value > expectedValue) {
				do {
					distinct.click();
					value = Integer.parseInt(currentValue.getText());
					currentValue.click();
				} while (value > expectedValue);
			}
		}
	}

	@Step("Set checkbox \" I`am traveling for work\"")
	public BookingPage setTravelingForWorkCheckbox() {
		checkboxTravelForWork.click();
		return this;
	}

	@Step("Press search button")
	public ResultPage clickSearch() {
		searchButton.click();
		return new ResultPage(webDriver);
	}
}
