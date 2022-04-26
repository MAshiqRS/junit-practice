import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class JunitSeleniumJava {
    WebDriver driver;
    @Before
    public void setup(){
        System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver.exe");
        FirefoxOptions ops=new FirefoxOptions();
        ops.addArguments("--headed");
        driver=new FirefoxDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    // method for getting the website title
    @Test
    public void getTitle(){
        driver.get("https://demoqa.com/");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertEquals("ToolsQA",title);
    }

    //method for checking a specific image present in the website
    @Test
    public void checkImgExistance(){
        driver.get("https://demoqa.com/");
        //using cssSelector
//        WebElement image1= driver.findElement(By.cssSelector("img"));
//        Assert.assertTrue(String.valueOf(image1.isDisplayed()),true);

        //using Xpath
        WebElement image2 = driver.findElement(By.xpath("//header/a[1]/img[1]"));
        Assert.assertTrue(String.valueOf(image2.isDisplayed()),true);

    }

    // wirte on textbox
    @Test
    public void writeOnTextbox(){
        driver.get("https://demoqa.com/text-box");
        //using id locator
        WebElement txtUsername = driver.findElement(By.id("userName"));
        // using css selector locator *text type
       // WebElement txtUsername = driver.findElement(By.cssSelector("[type=text]"));
        // using css selector locator *placeholder type
//        WebElement txtUsername = driver.findElement(By.cssSelector("[placeholder='Full Name']"));
        //using tagname locator and its default index zero as selection priority
//        WebElement txtUsername = driver.findElement(By.tagName("input"));
//        List<WebElement> elements = driver.findElements(By.tagName("input"));
//        elements.get(0).sendKeys("Rahman");
//        elements.get(1).sendKeys("abc@com.in");


        txtUsername.sendKeys("Hello World");
        driver.findElement(By.id("userEmail")).sendKeys("abc@test.com");
        driver.findElement(By.id("submit")).click();

    }
//            different type of click in button
    @Test
    public void clickButton(){
        driver.get("https://demoqa.com/buttons");

        //regular dynamic click
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        buttons.get(3).click();

        //double click
        Actions actions = new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();

        //right click
        actions.contextClick(buttons.get(2)).perform();

        //ASSERTION FOR ALL CLICKS
        String  doubleClickMsg = driver.findElement(By.id("doubleClickMessage")).getText();
        String  rightClickMsg = driver.findElement(By.id("rightClickMessage")).getText();
        String  dynamicClickMsg = driver.findElement(By.id("dynamicClickMessage")).getText();
        Assert.assertTrue(doubleClickMsg.contains("You have done a double click"));
        Assert.assertTrue(rightClickMsg.contains("You have done a right click"));
        Assert.assertTrue(dynamicClickMsg.contains("You have done a dynamic click"));

    }

//            different kind of alert handler
    @Test
    public void alertHandle() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
    }
    @Test
    public void alertHandleWithDelay() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();
        //Thread.sleep(7000);
        driver.switchTo().alert().accept();
    }
    @Test
    public void dialogBoxHandle() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("confirmButton")).click();
        //Thread.sleep(2000);
        //driver.switchTo().alert().accept();
        driver.switchTo().alert().dismiss();
    }
    @Test
    public void promptHandler() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();
        //Thread.sleep(2000);
        //driver.switchTo().alert().accept();
        driver.switchTo().alert().sendKeys("Hello World");
        driver.switchTo().alert().accept();
    }

//            date selection

    @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("01/01/1000");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);

    }

//            dropdown selection single & multiple
//    single
    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select options = new Select(driver.findElement(By.id("oldSelectMenu")));
//        options.selectByValue("3");
//        options.selectByIndex(2);
        options.selectByVisibleText("Green");
//        driver.findElement(By.id("oldSelectMenu")).click(); // cannot access dropdown value by click() function

    }
//    multiple
    @Test
    public void selectMultipleDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select options = new Select(driver.findElement(By.id("cars")));
        if(options.isMultiple()){
            options.selectByValue("volvo");
            options.selectByValue("audi");
        }
    }

//        handling multiple tabs
    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
//        switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New Tab is: "+ driver.getTitle());
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text,"This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));

    }
//        NEW WINDOW HANDLING
    @Test
    public void windowHandling(){
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("windowButton")).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        while (iterator.hasNext()){
            String childWindow = iterator.next();
            if(!mainWindowHandle.equalsIgnoreCase(childWindow)){
                driver.switchTo().window(childWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }
        }
    }
//        modal dialog handling
    @Test
    public void modalDialog(){
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String text = driver.findElement(By.className("modal-body")).getText();
        System.out.println(text);
        driver.findElement(By.id("closeSmallModal")).click();

    }

//        data get from tabel ,add to web table
    @Test
    public void scrapData(){
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-table"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i =0 ;
        for (WebElement row : allRows){
            List<WebElement>cells = row.findElements(By.className("rt-td"));
            for(WebElement cell: cells){
                i++;
                System.out.println("num["+i+"] " + cell.getText());
            }
        }
    }
//            upload image/file
    @Test
    public void uploadImg(){
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("F:\\Java_Practice\\Selenium_Junit_Practice\\src\\test\\resources\\demo.jpg");

    }
//        frame handling
    @Test
    public void handleIFrame(){
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println(text);
        driver.switchTo().defaultContent();
    }

//            hover on element
    @Test
    public void mouseHover(){
        driver.get("https://green.edu.bd/");
//        using xpath
//        WebElement aboutUs =  driver.findElement(By.xpath("//a[@class='dropdown-toggle'][contains(text(),'About Us')]"));

        List<WebElement> aboutUs =  driver.findElements(By.xpath("//a[contains(text(),'About Us')]"));

//        using linktext
//        WebElement aboutUs =  driver.findElement(By.linkText("ABOUT US"));
        Actions actions = new Actions(driver);
//        actions.moveToElement(aboutUs).perform();
        actions.moveToElement(aboutUs.get(2)).perform();
    }

//            keyboard event shift click , right click

    @Test
    public void keyboardEvent() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions actions = new Actions(driver);
//        actions.moveToElement(searchElement).perform();
//        actions.keyDown(Keys.SHIFT).perform();
//        actions.sendKeys("selenium webdriver").perform();
//        actions.keyUp(Keys.SHIFT).perform();
//        actions.doubleClick(searchElement).perform();
//        actions.contextClick(searchElement).perform();

//                chining line 271 to 276
        actions.moveToElement(searchElement).
                keyDown(Keys.SHIFT).
                sendKeys("selenium webdriver").
                keyUp(Keys.SHIFT).
                doubleClick(searchElement).
                contextClick(searchElement).perform();

        Thread.sleep(5000);
    }
//            screenshot take of a page
    @Test
    public void takeScreenshot() throws IOException {
//        driver.get("https://demoqa.com/");
        driver.get("https://www.instagram.com/");
        File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yy-hh-mm-ss-aa").format(new Date());
        String fillWithPath = "./src/test/resources/screenshots/" + time +".png";
        File destFile = new File(fillWithPath);
        FileUtils.copyFile(screenShotFile, destFile);

    }

//    read data from excal

    public  void readFromExcel(String filePath,String fileName,String sheetName) throws IOException {
        File file = new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if(fileExtensionName.equals(".xls")){
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
        for (int i = 0; i < rowCount+1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                DataFormatter formatter = new DataFormatter();
                System.out.print(formatter.formatCellValue ((row.getCell(j)))+"|| ");
            }
            System.out.println();

        }
    }


    @Test
    public void readExcelFile() throws IOException {
        String filePath = "./src/test/resources";
        readFromExcel(filePath,"prac.xlsx","Sheet1");
    }





    @After
    public void closeDriver(){
        driver.close();
    }

}
