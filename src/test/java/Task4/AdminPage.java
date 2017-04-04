package Task4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.ChromeDriverManager;


public class AdminPage {
    private static final String BASE_URL= "http://172.22.89.65";
    private static final String PAGE_URL= "/litecart/admin/login.php";
    private WebDriver driver;
    private WebDriverWait wait;

    private By parentContainerSelector = By.cssSelector("ul[class=list-vertical]");
    private By childContainerSelector = By.cssSelector("ul[class=docs]");
    private By parentItemSelector = By.cssSelector("li[id=app-]");
    private By childItemSelector = By.tagName("li");
    private By headerSelector = By.cssSelector("h1");

    @Before
    public void start() {

        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void testSelenium() throws IOException {
        driver.get(BASE_URL+PAGE_URL);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement parentul = driver.findElement(parentContainerSelector);
        int parentItemsListSize = parentul.findElements(By.cssSelector("li[id=app-]")).size();

        for (int i = 0; i < parentItemsListSize; i++) {
            WebElement parentContainer = driver.findElement(parentContainerSelector);
            List<WebElement> pl = parentContainer.findElements(parentItemSelector);
            WebElement parentMenuItem = pl.get(i);
            parentMenuItem.click();
            wait.withTimeout(5000, TimeUnit.MILLISECONDS);
            if (checkElementPresent(childContainerSelector)) {
                clickAllSelectedChildren(childContainerSelector, childItemSelector);
            } else {
                System.err.println("no children");
            }

        }
    }


    public void clickAllSelectedChildren(By parentselector, By childselector){
        WebElement element = driver.findElement(parentselector);
        int childItemListSize = element.findElements(childselector).size();
        for (int i = 0; i < childItemListSize; i++) {
            WebElement h = driver.findElement(parentselector);
            List<WebElement> webElements = h.findElements(childselector);
            WebElement childMenuItem = webElements.get(i);
            String header = childMenuItem.getText();
            childMenuItem.click();
            validateHeader(header);

        }

    }

    public boolean checkElementPresent(By selector){
        int size = driver.findElements(selector).size();
        boolean result = size != 0;
        return result;

    }

    private void validateHeader(String header) {
        System.out.println("child menu item (expected): "+ header);
        WebElement h = driver.findElement(headerSelector);
        String title = h.getText();
        System.out.println("page header (actual): "+ title);
        if(header!=null && header.contains(title)) System.out.println("Page title correct!");
        else System.err.println("Page title incorrect!");

    }




    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}