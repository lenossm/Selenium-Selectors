import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ge.tbcitacademy.data.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LocatorsTest {
    private WebDriver driver;

    // ბრაუზერის ინიციალიზაცია თითოეული ტესტის დაწყებამდე
    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    // ბრაუზერის დახურვა თითოეული ტესტის დასრულების შემდეგ
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void unorderedListTest() {
        // საიტზე გადასვლა
        driver.get(Constants.JQUERY_UI_SLIDER_URL);

        // aside ელემენტის მოძებნა კონკრეტული h3 ელემენტით
        WebElement asideElement = driver.findElement(By.xpath(
                "//aside[h3[contains(@class,'widget-title') and text()='Effects']]"
        ));

        // aside-ის შიგნით ყველა li ელემენტის მოძებნა
        List<WebElement> listItems = asideElement.findElements(By.cssSelector("ul > li"));

        // Stream API - ტექსტში "o"-ს შემცველი ელემენტების ფილტრაცია
        List<WebElement> filteredList = listItems.stream()
                .filter(e -> e.getText().contains("o"))
                .collect(Collectors.toList());

        // ფილტრირებული სიაზე პარალელური გადამუშავება და href-ის დაბეჭდვა
        filteredList.parallelStream().forEach(item -> {
            WebElement link = item.findElement(By.tagName("a"));
            String href = link.getAttribute("href");

            // "animate"-ის შემცველი href-ების გამოტოვება
            if (!href.contains("animate")) {
                System.out.println(href);
            }
        });
    }

    @Test
    public void buttonsTest() {
        // საიტზე გადასვლა
        driver.get(Constants.ADD_REMOVE_ELEMENTS_URL);

        // Add Element ღილაკზე სამჯერ დაკლიკება
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));
        addButton.click();
        addButton.click();
        addButton.click();

        // ბოლო Delete ღილაკის მოძებნა XPath last() ფუნქციით
        WebElement lastDeleteButton = driver.findElement(By.xpath("(//button[text()='Delete'])[last()]"));

        // class ატრიბუტის ვალიდაცია
        Assert.assertEquals(lastDeleteButton.getAttribute("class"), "added-manually");

        // ყველა Delete ღილაკის მოძებნა cssSelector-ით onclick ატრიბუტის მიხედვით
        List<WebElement> deleteButtons = driver.findElements(By.cssSelector("button[onclick^='deleteElement']"));

        // ბოლო ღილაკის ამოღება სიიდან
        WebElement lastButton = deleteButtons.get(deleteButtons.size() - 1);

        // onclick ატრიბუტის ვალიდაცია
        Assert.assertEquals(lastButton.getAttribute("onclick"), "deleteElement()");
    }

    @Test
    public void challengingDomTest() {
        // საიტზე გადასვლა
        driver.get(Constants.CHALLENGING_DOM_URL);

        // Apeirian9-ის გვერდით მყოფი Lorem value-ის მოძებნა XPath-ით
        WebElement loremValue = driver.findElement(By.xpath(
                "//td[text()='Apeirian9']/preceding-sibling::td[1]"
        ));

        // შედეგის დაბეჭდვა
        System.out.println("Lorem value for Apeirian9 is: " + loremValue.getText());
    }
}
