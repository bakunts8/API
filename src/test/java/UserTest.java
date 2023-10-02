import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.UserService;
import users.UserData;

import java.util.HashSet;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserTest extends BaseTest {

    @Test
    public void getUsersTest() {
        UserService service = new UserService();
        List<UserData> users = List.of(service.getUsers("/users")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("JS_validation.json"))
                .extract()
                .as(UserData[].class));

        HashSet<Integer> uniqueId = new HashSet<>();
        HashSet<String> uniqueEmail = new HashSet<>();

        for (UserData user : users) {
            uniqueId.add(user.getId());
            uniqueEmail.add(user.getEmail());
        }
        Assert.assertEquals(users.size(), uniqueId.size(), "users id repeat");
        Assert.assertEquals(users.size(), uniqueEmail.size(), "users email repeat");
    }

    @Test()
    public void createUserTest() throws JsonProcessingException {
        UserService service = new UserService();
        UserData forCreate = new UserData().giveNewUser();

        UserData created = service.createUser("/users", forCreate)
                .then()
                .statusCode(201)
                .body("id", Matchers.isA(Integer.class))
                .body("email", Matchers.matchesPattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$"))
                .extract().as(UserData.class);

        Assert.assertEquals(forCreate, created, "The user was created incorrectly");
    }

    @Test()
    public void updateUserTest() throws JsonProcessingException {
        UserService service = new UserService();
        Faker fake = new Faker();
        int userId = service.getRandomId();
        UserData forUpdate = service.getUser(userId);
        forUpdate.setEmail(fake.internet().emailAddress());

        UserData updated = service.updateUser("/users/" + userId, forUpdate)
                .then()
                .statusCode(200)
                .extract()
                .as(UserData.class);

        Assert.assertEquals(forUpdate, updated, "The user was updated incorrectly");
    }

    @Test()
    public void deleteUserTest() {
        UserService service = new UserService();
        int userId = service.getRandomId();
        service.deleteUser("/users/" + userId)
                .then()
                .statusCode(204);
    }
}