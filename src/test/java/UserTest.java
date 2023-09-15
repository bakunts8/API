import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import users.UserData;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserTest extends BaseTest {

    @Test
    public void getUsersTest() {
        String endpoint = "/users";
        List<UserData> users = given()
                .when()
                .get(endpoint)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("JS_validation.json"))
                .extract().body().jsonPath().getList("", UserData.class);

        Assert.assertEquals(users.size(), 10);
    }

    @Test(priority = 1)
    public void createUserTest(ITestContext id) throws JsonProcessingException {
        String endpoint = "/users";
        Faker faker = new Faker();

        UserData newUser = new UserData();
        newUser.setName(faker.name().name());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setGender(faker.dog().gender());
        newUser.setStatus("active");

        UserData actualUser = given()
                .body(newUser.serialize(newUser))
                .when()
                .post(endpoint)
                .then()
                .log()
                .all()
                .statusCode(201)
                .body("id", Matchers.isA(Integer.class))
                .body("email", Matchers.containsString("@"))
                .extract().as(UserData.class);
        id.setAttribute("id", actualUser.getId());

        Assert.assertEquals(actualUser, newUser);
    }

    @Test(priority = 2)
    public void updateUserTest(ITestContext id) throws JsonProcessingException {
        String endpoint = "/users/" + id.getAttribute("id");
        Faker faker = new Faker();

        UserData updatedUser = new UserData();
        updatedUser.setName(faker.name().name());
        updatedUser.setEmail(faker.internet().emailAddress());
        updatedUser.setGender(faker.dog().gender());
        updatedUser.setStatus("active");

        UserData actualUser = given()
                .body(updatedUser.serialize(updatedUser))
                .when()
                .patch(endpoint)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("name", equalTo(updatedUser.getName()))
                .body("email", equalTo(updatedUser.getEmail()))
                .extract().as(UserData.class);

        Assert.assertEquals(actualUser, updatedUser);
    }

    @Test(priority = 3)
    public void deleteUserTest(ITestContext id) throws JsonProcessingException {
        String endpoint = "/users/" + id.getAttribute("id");

        given()
                .delete(endpoint)
                .then()
                .log()
                .all()
                .statusCode(204);
    }
}
