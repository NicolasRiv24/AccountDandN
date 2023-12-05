import com.aventstack.extentreports.ExtentTest;
import org.example.UserConfig;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import static org.hamcrest.Matchers.*;
public class HappyPathTest {

    ExtentReports extent = new ExtentReports();
    ExtentSparkReporter spark = new ExtentSparkReporter("Extentreport.html");
@BeforeTest
    public void beforeTest(){
        extent.attachReporter(spark);

    }

@AfterTest
    public void afterTest(){
        extent.flush();
    }

    @Test (priority = 0)
    void PostCreateUser() {

        ExtentTest test = extent.createTest("Status Code 201 when creating a user");

        try{
        UserConfig.UserID = given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/User")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("userID");

        test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
        System.out.println("UserID: " + UserConfig.UserID);


    }

    @Test(priority = 1)
    void PostUserUnauthorized() {
        ExtentTest test = extent.createTest("Status code 200 when asking if the user is unauthorized");

        try {
            // Your test logic
            given()
                    .contentType("application/json")
                    .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                    .when()
                    .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                    .then()
                    .statusCode(200)
                    .and()
                    .body(equalTo("false"))
                    .log()
                    .all(); // Log the response for debugging

            // If the above steps complete without exceptions, log the test as passed
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }


    @Test (priority = 3)
    void PostGenerateToken() {
        ExtentTest test = extent.createTest("Status code 200 when generating the token");

        UserConfig.Token = null;
        var x = given()
                    .contentType("application/json")
                    .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                    .post("https://bookstore.toolsqa.com/Account/v1/GenerateToken")
                .then();
        try {
            x.statusCode(201);
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }finally {
            UserConfig.Token = x.extract().jsonPath().getString("token");
        }
        System.out.println("token: " + UserConfig.Token);
    }
    @Test (priority = 4)
    void PostUserAuthorized(){
            ExtentTest test = extent.createTest("Status code 200 when asking if user is Authorized");

            try {
        given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                .then()
                .statusCode(200)
                .and()
                .body(equalTo("true"));
                test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }

    }
    @Test (priority = 5)
    void GetUserAuthorized(){
        ExtentTest test = extent.createTest("Status code 200 when getting a user");

        try {
        given().contentType("application/json")
                .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                .get("https://bookstore.toolsqa.com/Account/v1/User/" + UserConfig.UserID)
                .then()
                .statusCode(200);
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }
    @Test (priority = 6)
    void DeleteUserAuthorized(){
        ExtentTest test = extent.createTest("Status code 204 when deleting a user");

        try {
        given().contentType("application/json")
                .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                .delete("https://bookstore.toolsqa.com/Account/v1/User/" + UserConfig.UserID)
                .then()
                .statusCode(204);
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            // If any exception occurs, log the test as failed
            test.log(Status.FAIL, "Test Failed!");
            // You can also log the exception message for more details
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }
}
