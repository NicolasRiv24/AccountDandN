import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.example.UserConfig;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;
public class BA11Delete {
}

/*borrar*/
    /*
@Test(priority = 1)
void PostUserUnauthorized() {
    ExtentTest test = extent.createTest("Status code 200 when user is Unauthorized");

    try {
        // Your test logic
        given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                .then()
                .statusCode(240)
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
}*/