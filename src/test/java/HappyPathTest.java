import org.example.UserConfig;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;
public class HappyPathTest {

    @Test (priority = 0)
    void PostCreateUser() {

        UserConfig.UserID = given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/User")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("userID");

        System.out.println("UserID: " + UserConfig.UserID);
    }
    @Test (priority = 1)
    void PostUserUnauthorized(){
        given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                .then()
                .statusCode(200)
                .and()
                .body(equalTo("false"));


    }
    @Test (priority = 3)
    void PostGenerateToken() {

        UserConfig.Token = given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/GenerateToken")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        System.out.println("token: " + UserConfig.Token);
    }
    @Test (priority = 4)
    void PostUserAuthorized(){
        given()
                .contentType("application/json")
                .body("{\"userName\": \"" + UserConfig.UserName + "\", \"password\": \"" + UserConfig.Password + "\"}")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                .then()
                //.statusCode(200)
                //.and()
                .body(equalTo("true"));


    }
    @Test (priority = 5)
    void GetUserAuthorized(){
        given().contentType("application/json")
                .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                .get("https://bookstore.toolsqa.com/Account/v1/User/" + UserConfig.UserID)
                .then()
                .statusCode(200);
    }
    @Test (priority = 6)
    void DeleteUserAuthorized(){
        given().contentType("application/json")
                .header("Authorization", "Bearer " + UserConfig.Token)
                .when()
                .delete("https://bookstore.toolsqa.com/Account/v1/User/" + UserConfig.UserID)
                .then()
                .statusCode(204);
    }
}
