import org.example.UserConfig;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import java.lang.reflect.Array;
import static org.hamcrest.Matchers.*;
import org.json.JSONObject;
import com.aventstack.extentreports.ExtentTest;
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

        ExtentTest test = extent.createTest("Status Code 201 when creating a user")
                        .assignAuthor("Diego").assignCategory("Account Creation");



        try{
        UserConfig.UserID =
                given()
                    .contentType("application/json")
                    .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                    .post(UserConfig.ACCOUNTURL+"User")
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
        ExtentTest test = extent.createTest("Status code 200 when asking if the user is unauthorized")
                        .assignAuthor("Nicolas").assignCategory("User Unauthorized");
        try {
                given()
                        .contentType("application/json")
                        .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                        .post(UserConfig.ACCOUNTURL+"Authorized")
                .then()
                        .statusCode(200)
                        .and()
                        .body(equalTo("false"))
                        .log()
                        .all();
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed!");
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }

    @Test (priority = 3)
    void PostGenerateToken() {
        ExtentTest test = extent.createTest("Status code 200 when generating the token")
                        .assignAuthor("Diego").assignCategory("Token Generation");
        UserConfig.Token = null;
        var x = given()
                    .contentType("application/json")
                    .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                    .post(UserConfig.ACCOUNTURL + "GenerateToken")
                .then();
        try {
            x.statusCode(201);
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed!");
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }finally {
            UserConfig.Token = x.extract().jsonPath().getString("token");
        }
        System.out.println("token: " + UserConfig.Token);
    }

    @Test (priority = 4)
    void PostUserAuthorized(){
            ExtentTest test = extent.createTest("Status code 200 when asking if user is Authorized")
                            .assignAuthor("Nicolas").assignCategory("User Authorized");
        try {
                given()
                    .contentType("application/json")
                    .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                    .post(UserConfig.ACCOUNTURL+"Authorized")
                .then()
                    .statusCode(200)
                    .and()
                    .body(equalTo("true"));
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed!");
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }

    @Test (priority = 5)
    void GetUserAuthorized(){
        ExtentTest test = extent.createTest("Status code 200 when getting a user")
                .assignAuthor("Diego").assignCategory("Get User Info");
        try {
                given().contentType("application/json")
                    .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                    .get(UserConfig.ACCOUNTURL + "User/" + UserConfig.UserID)
                .then()
                    .statusCode(200);
            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed!");
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }
    @Test (priority = 6)
    void DeleteUserAuthorized(){
        ExtentTest test = extent.createTest("Status code 204 when deleting a user")
                        .assignAuthor("Nicolas").assignCategory("Delete User");
        try {
                given().contentType("application/json")
                    .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                    .delete(UserConfig.ACCOUNTURL + "User/" + UserConfig.UserID)
                .then()
                    .statusCode(204);

            test.log(Status.PASS, "Test Passed!");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed!");
            test.fail("Exception details: " + e.getMessage());
            throw e;
        }
    }
}
