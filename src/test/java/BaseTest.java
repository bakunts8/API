import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setURI() {
        RestAssured.requestSpecification = new RequestSpecBuilder().build()
                .baseUri("https://gorest.co.in/public/v2")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer 74542eb10c462b74a8248777b4a53bcf87953471a16a7c20c3c9d730af0a6ec8");
    }
}