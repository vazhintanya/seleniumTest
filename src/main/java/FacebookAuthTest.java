import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FacebookAuthTest {
    protected Properties prop = new Properties();

    public FacebookAuthTest() throws IOException {
        String propFileName = "auth.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if (inputStream == null) {
            throw new FileNotFoundException("property file " + propFileName + " not found in the classpath");
        }

        prop.load(inputStream);
    }

    @Test
    public void checkAuth() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        WebDriver facebook = new ChromeDriver();

        facebook.get("https://www.facebook.com/");
        facebook.manage().window().fullscreen();

        By mainPage = By.xpath("//a[@href='/me/' and @role='link']");

        facebook.findElement(By.xpath("//input[@data-testid='royal_email']"))
                .sendKeys(prop.getProperty("user"));
        facebook.findElement(By.xpath("//input[@data-testid='royal_pass']"))
                .sendKeys(prop.getProperty("password"));
        facebook.findElement(By.xpath("//button[@data-testid='royal_login_button']")).click();


        Assert.assertNotNull(facebook.findElement(mainPage));
        facebook.quit();
    }

    @Test
    public void checkIncorrectAuth() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        WebDriver facebook = new ChromeDriver();

        facebook.get("https://www.facebook.com/");
        facebook.manage().window().fullscreen();

        By forgotPswrd = By.xpath
                ("//a[@href='https://www.facebook.com/recover/initiate?lwv=120&lwc=1348092&ars=facebook_login_pw_error']");

        facebook.findElement(By.xpath("//input[@data-testid='royal_email']"))
                .sendKeys(prop.getProperty("user"));
        facebook.findElement(By.xpath("//input[@data-testid='royal_pass']"))
                .sendKeys("123");
        facebook.findElement(By.xpath("//button[@data-testid='royal_login_button']")).click();
        Assert.assertEquals("Забыли пароль?", facebook.findElement(forgotPswrd).getText());

        facebook.quit();

    }
}
