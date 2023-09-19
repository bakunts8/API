package users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.util.Objects;

public class UserData {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;

    public String serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }

    public UserData giveNewUser() {
        Faker faker = new Faker();
        setName(faker.name().name());
        setEmail(faker.internet().emailAddress());
        setGender(faker.dog().gender());
        setStatus("active");
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(name, userData.name) && Objects.equals(email, userData.email) && Objects.equals(gender, userData.gender) && Objects.equals(status, userData.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, gender, status);
    }

    @Override
    public String toString() {
        return "{\n" +
                "  name='" + name + '\n' +
                "  email='" + email + '\n' +
                "  gender='" + gender + '\n' +
                "  status='" + status + '\n' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

