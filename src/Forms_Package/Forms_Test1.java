package Forms_Package;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.codecs.BooleanCodec;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Forms_Test1 {
	public static WebDriver driver;
	public static ExtentReports reports;
	static ExtentTest logger;
	String primaryWindow;

	@AfterTest
	public static void tearDownDriver() throws InterruptedException {
		Thread.sleep(1000);
		reports.flush();
		driver.quit();
	}

	@BeforeTest
	public static void setUpReports() {
		// String filename = "CAPS" + String.valueOf(LocalDateTime.now()) + ".html";
		String path = "C:\\Users\\spatel\\Documents\\Forms_ExtentReport\\" + "Forms.html";
		reports = new ExtentReports(path);
	}

	@BeforeTest
	public static void setUpDriver() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\spatel\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://iwe-test.uclanet.ucla.edu/Forms/dashboard");
	}

	@Test(priority = 1)
	public static void TC01_TestLogin() throws InterruptedException {
		logger = reports.startTest("Test Login");
		driver.findElement(By.id("logonId")).sendKeys("pdugar");
		logger.log(LogStatus.INFO, "Entered User Logon Id");
		driver.findElement(By.id("passPhrase")).sendKeys("lettuce tall soft tasty");
		logger.log(LogStatus.INFO, "Entered User Phrase");
		driver.findElement(By.xpath("//input[@class='searchSubmit']")).click();
		logger.log(LogStatus.INFO, "Clicked Submit Button");
		driver.findElement(By.xpath("//input[@id='forwardReq']")).click();
		logger.log(LogStatus.INFO, "Clicked Forward Request");
		logger.log(LogStatus.PASS, "Login Passed : Success");

	}

	@Test(priority = 2)
	public static void TC02_CreateNewForm() throws InterruptedException {
		logger = reports.startTest("Test Create New Form");
		WebElement createNewForm = driver.findElement(By.xpath("//div[@class='iconButtonSection float']"));
		Assert.assertEquals(createNewForm.isDisplayed(), true);
		logger.log(LogStatus.PASS, "Create New Form Button is Displayed");
		Assert.assertEquals(createNewForm.isEnabled(), true);
		logger.log(LogStatus.PASS, "Create New Form Button is Enabled");
		createNewForm.click();
		logger.log(LogStatus.INFO, "Clicked on Create New Form Button");
		WebElement ddOrganisation = driver.findElement(By.xpath("//iwe-dropdown2[@id='dept']"));
		System.out.println("Test default selected value");
		// Test default selected value
		Assert.assertEquals(ddOrganisation.getAttribute("input_value"), "-1");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Select Organization type
		js.executeScript("arguments[0].setAttribute('input_value','30');", ddOrganisation);
		logger.log(LogStatus.PASS, "Selected Organisation type");
		WebElement continueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
		continueButton.click();
		WebElement formTitle = driver.findElement(By.xpath("//input[@id='fakeTitleText']"));
		// Assert.assertEquals(formTitle.isDisplayed(), true);
		formTitle.clear();
		formTitle.sendKeys("Test Form Created " + LocalDate.now().toString());
		Thread.sleep(1000);
		logger.log(LogStatus.PASS, "New Form Created : Success ");
		driver.findElement(By.xpath("//div[contains(text(),'My Forms')]")).click();

	}

	@Test(priority = 3)
	public static void TC03_TestSearchAndOpenForm() throws InterruptedException {
		logger = reports.startTest("Test Search And Open New Form");
		WebElement searchElement = driver.findElement(By.xpath("//iwe-input[@id='searchTerm']"));
		searchElement.click();
		searchElement.sendKeys("Test Form Created ");
		logger.log(LogStatus.INFO, "Search for New Form ");
		Thread.sleep(400);
		WebElement searchFormList = driver.findElement(By.xpath("//div[@id='formList']"));
		System.out.println("Form list div found");
		List<WebElement> formList = searchFormList.findElements(By.xpath("//div[@class='listRow']"));
		Assert.assertTrue(!formList.isEmpty(), "Failed: New form Creation");
		System.out.println(" div rows found" + formList.get(0).toString());
		formList.get(0).findElement(By.xpath("//div[@class='listRow']//a")).click();
		logger.log(LogStatus.PASS, "Search and Open Form : Success ");
		// Go to Layout tab
		driver.findElement(By.xpath("//div[contains(text(),'Layout')]")).click();
		logger.log(LogStatus.INFO, "Selected- Layout tab");
	}

	@Test(priority = 4, dataProvider = "getDataForTextQuestion")
	public static void TC04_AddNewTextQuestionToFormLayout(String questionType, String labelText, String helpText,
			String minLength, String maxLength, String isNumbersOnly, String isCharsOnly, String isRequired,
			String validData) throws InterruptedException {
		logger = reports.startTest("Test Create New Question - Text");
		// click New Question
		Thread.sleep(2000);
		WebElement newQuesElement = driver.findElement(By.xpath("//i[@class='fa-plus fa-2x']"));
		newQuesElement.click();
		logger.log(LogStatus.INFO, "Clicked- New Question Button ");
		WebElement divAllElements = driver.findElement(By.xpath("//div[@id='allElements']"));
		int size = divAllElements.findElements(By.xpath("//div[@id='outsideFormQuestion']")).size();
		System.out.println("Size " + size);
		WebElement saveQuestionElement = driver
				.findElement(By.xpath("//div[@id='allElements']//div[" + size + "]//button[@id='save']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", saveQuestionElement);
		// Thread.sleep(1000);
		WebElement ddQuestionType = driver.findElement(
				By.xpath("//div[@id='allElements']//div[" + size + "]//iwe-dropdown2[@label_text='Question Type']"));
		// Select Organization type
		js.executeScript("arguments[0].setAttribute('input_value','" + questionType + "');", ddQuestionType);
		logger.log(LogStatus.PASS, "Selected Question type");
		// driver.switchTo().window(parentWindow);
		WebElement labelTextElement = driver
				.findElement(By.xpath("//div[@id='allElements']//div[" + size + "]//iwe-input[@input_id='label']"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('input_value','" + labelText + "');", labelTextElement);
		logger.log(LogStatus.INFO, "Entered Label text");
		WebElement helpTextElement = driver
				.findElement(By.xpath("//div[@id='allElements']//div[" + size + "]//iwe-input[@input_id='help']"));
		js.executeScript("arguments[0].setAttribute('input_value','" + helpText + "');", helpTextElement);
		logger.log(LogStatus.INFO, "Entered Help text");
		// Enter Minimum length
		WebElement minlengthText = driver
				.findElement(By.xpath("//div[@id='allElements']//div[" + size + "]//iwe-input[@input_id='minLength']"));
		js.executeScript("arguments[0].setAttribute('input_value','" + minLength + "');", minlengthText);
		logger.log(LogStatus.INFO, "Entered Minimum length");
		// Enter Maximum length
		WebElement maxlengthText = driver
				.findElement(By.xpath("//div[@id='allElements']//div[" + size + "]//iwe-input[@input_id='maxLength']"));
		js.executeScript("arguments[0].setAttribute('input_value','" + maxLength + "');", maxlengthText);
		logger.log(LogStatus.INFO, "Entered Maximum Length");
		// Save Question
		if (size == 1) {
			saveQuestionElement.click();
		} else {
			driver.findElement(By.xpath("//div[@id='allElements']//div[1]")).click();
		}
		logger.log(LogStatus.PASS, "New Question Type Text Created :Success ");
		// Save Form
		WebElement divAllElementsNew = driver.findElement(By.xpath("//div[@id='allElements']"));
		if (validData == "Valid") {
			try {
				Assert.assertTrue(
						divAllElementsNew.findElements(By.xpath("//div[@id='outsideFormQuestion']")).size() == size,
						"Failed: Add New Question Type Text ");
				logger.log(LogStatus.PASS, "Form Layout Saved : Success ");

			} catch (Exception ex) {
				logger.log(LogStatus.FAIL, "Form Layout Saved : Fail ");
			}
		} else {
			try {
				logger.log(LogStatus.FAIL, "Form Layout Saved : Fail ");
			Assert.assertTrue(
					divAllElementsNew.findElements(By.xpath("//div[@id='outsideFormQuestion']")).size() != size,
					"Failed: Added New Question Type Text With invalid Data ");
			}
			catch (Exception ex) {
				logger.log(LogStatus.FAIL, "Form Layout Saved : Fail ");
			}
		}
		driver.findElement(By.xpath("//button[@class='iweButton primary-normal' and text()='Save']")).click();

	}

	@Test(priority = 5, enabled = true)
	public void TC05_TestViewForm() throws InterruptedException {
		logger = reports.startTest("Test View Form - Text");
		// View Form
		primaryWindow = driver.getWindowHandle();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@class='iweButton' and text()='View Form' ]")).click();
		driver.findElement(By.xpath("//button[@class='iweButton' and text()='View Form' ]")).click();
		// Thread.sleep(2000);
		logger.log(LogStatus.PASS, "View Form : Success ");

	}

	@Test(priority = 6, enabled = true)
	public void TC06_TestSubmitForm() throws InterruptedException {

		logger = reports.startTest("Submit View Form - Text");
		for (String handle : driver.getWindowHandles()) {
			System.out.println("No of handles " + handle);
			driver.switchTo().window(handle);
		}
		WebElement divElement = driver.findElement(By.xpath("//div[@id='elements']"));
		WebElement inputElement = divElement.findElements(By.xpath("//iwe-input")).get(0);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('input_value','Answer 1');", inputElement);

		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@id='btnSubmit']")).click();
		WebElement thanksMsgElement = driver.findElement(By.xpath("//iwe-message[@id='secThanks']"));
		Assert.assertTrue(thanksMsgElement.getText().contains("Thank you"), "Failed: Form Submit");
		logger.log(LogStatus.PASS, "Submit Form : Success ");
	}

	@Test(priority = 7, enabled = true)
	public void TC07_TestResponseSubmitted() throws InterruptedException {
		logger = reports.startTest("Test Response Submitted ");
		driver.switchTo().window(primaryWindow);
		// Go to Response tab
		driver.findElement(By.xpath("//div[contains(text(),'Responses')]")).click();
		logger.log(LogStatus.INFO, "Selected- Responses tab");
		driver.findElement(By.xpath("//tr[@class='dx-row dx-data-row dx-column-lines cursorClass']")).click();
		logger.log(LogStatus.PASS, "Open Latest Response : Successs");
	}

	@DataProvider(name = "getDataForTextQuestion")
	public Object[][] getDataForTextQuestion() {
		return new Object[][] { // labelText, helpText, minLength, maxLength, isNumbersOnly, isCharsOnly ,
								// isRequired
				{ "Text", "Question 1", "Help text 1", "2", "20", "false", "true", "true", "Valid" },
				{ "Text", "Question 2", "Help text 2", "15", "5", "true", "false", "true", "Invalid" },
				// { 2, "Paragraph Text", "Question 2", "Help text 2", "2", "20", "false",
				// "true", "true" }
		};
	}

}
