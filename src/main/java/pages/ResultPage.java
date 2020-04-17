package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ResultPage {

	public WebDriver webDriver;

	@FindBy(xpath = "//div[@class='bui-review-score__badge']")
	public List<WebElement> rate;

	@FindBy(xpath = "//div[contains(@class,'bui-price-display__value')]")
	public List<WebElement> price;

	@FindBy(xpath = "//a[@class='bui-pagination__link sr_pagination_link']/div[@class='bui-u-inline']")
	public List<WebElement> paginationLinks;

	@FindBy(xpath = "//a[contains(@class,'paging-next')]")
	public WebElement nextPage;

	public ResultPage(WebDriver driver) {
		this.webDriver = driver;
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 5), this);
	}

	public boolean isResultHasAcceptableConditions() {
		boolean result = false;
		for (int i = 1; i < getQuantityOfPages(); i++) {
			List<WebElement> prices = price;
			List<WebElement> marks = rate;
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			js.executeScript("arguments[0].scrollIntoView();", nextPage);
			try {
				result = matchValues(prices, marks);
			} catch (Exception e) {
				result = true;
				break;
			}
			clickNextPage();
		}
		return result;
	}

	private void clickNextPage() {
		nextPage.click();
		WebDriverWait wait = new WebDriverWait(webDriver, 5000);
		wait.until((ExpectedCondition<Boolean>) wdriver -> ((JavascriptExecutor) webDriver).executeScript("return jQuery.active == 0").equals(true));
	}

	private int getQuantityOfPages() {
		int result = 0;
		String stringLastPage;
		stringLastPage = paginationLinks.stream().skip(paginationLinks.size() - 1).findAny().orElse(null).getText();
		result = Integer.parseInt(stringLastPage.substring(stringLastPage.lastIndexOf(" ") + 1));
		return result;
	}

	private boolean matchValues(List<WebElement> prices, List<WebElement> markRate) throws Exception {
		int count = 0;
		List<Double> apartmentsRates = new ArrayList<>();
		for (WebElement price : prices) {
			String currentPrice = price.getText().substring(price.getText().lastIndexOf(" ") + 1);
			if (Integer.parseInt(currentPrice) > 205) {
				for (WebElement mark : markRate) {
					apartmentsRates.add(Double.parseDouble(mark.getText()));
				}
				if (apartmentsRates.get(count) > 8.1) {
					throw new Exception("Matched!");
				}
			}
			count++;
		}
		return false;
	}
}
