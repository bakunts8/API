package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import users.UserData;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserService {

    public Response getUsers(String path) {
        return given()
                .get(path)
                .then()
                .extract()
                .response();
    }

    public Response createUser(String path, UserData user) throws JsonProcessingException {
        return given()
                .body(user.serialize())
                .post(path)
                .then()
                .extract()
                .response();
    }

    public Response updateUser(String path, UserData user) throws JsonProcessingException {
        return given()
                .body(user.serialize())
                .patch(path)
                .then()
                .extract()
                .response();
    }

    public Response deleteUser(String path) {
        return given()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    public List<UserData> getUsersList() {
        return given()
                .get("/users")
                .then()
                .extract()
                .response()
                .body()
                .jsonPath()
                .getList("", UserData.class);
    }

    public UserData getUser(int id) {
        List<UserData> users = getUsersList();
        for (UserData user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public int getRandomId() {
        return getUsersList().get(new Random().nextInt(getUsersList().size() - 1)).getId();
    }
}