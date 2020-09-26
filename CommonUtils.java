package Utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static Utilities.Constants.BROWSER;


public class CommonUtils {

    public static void waitFor(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Works only for 2 windows
     * @param windowHandles
     */

    public void switchToWindow(){

        WebDriver driver = Utilities.Driver.getDriver(BROWSER);
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        String currentWindowHandle = Utilities.Driver.getDriver(BROWSER).getWindowHandle();
        while (iterator.hasNext()){
            String newHandle = iterator.next();
            if(!newHandle.equalsIgnoreCase(currentWindowHandle)){
                driver.switchTo().window(newHandle);
            }
        }

    }



    public static void takeScreenShot(WebDriver driver, String testName) throws IOException {

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = "screenshots/" + testName + System.currentTimeMillis() + ".png";
        File destScreenshot = new File(destination);
        FileUtils.copyFile(screenshot, destScreenshot);

    }

    public static Properties readPropertyFile (String pathToPropertyFile) throws IOException {

        Properties properties = new Properties();
        File credentialsFile = new File(pathToPropertyFile);
        FileInputStream fileInputStream = new FileInputStream(credentialsFile);
        properties.load(fileInputStream);

        return properties;
    }

    public static String getProperty(String key) {

        try {
            Properties properties = readPropertyFile("src/test/resources/Config/Configurations.properties");
            String value = properties.getProperty(key);
            return value;
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Could not read properties file");
        }

    }


    public static WebElement fluentWait(int duration, int pollingTime, By locator){

        Wait<WebDriver> wait = new FluentWait<>(Utilities.Driver.getDriver(getProperty(BROWSER)))
                .withTimeout(Duration.ofSeconds(duration))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoSuchElementException.class);


        WebElement element = wait.until(driver -> driver.findElement(locator));

        return element;

    }


        public static WebElement visibilityOfElement(int timeout, By locator){

        WebDriverWait wait = new WebDriverWait(Utilities.Driver.getDriver(getProperty(BROWSER)),timeout);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        return element;

    }

       public static WebElement elementToBeClickable(int timeout, By locator){

        WebDriverWait wait = new WebDriverWait(Utilities.Driver.getDriver(getProperty(BROWSER)),timeout);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        return element;

    }


        public static void waitForTitle(int timeout, String title){

        WebDriverWait wait = new WebDriverWait(Utilities.Driver.getDriver(getProperty(BROWSER)),timeout);
        wait.until(ExpectedConditions.titleIs(title));



    }








}
