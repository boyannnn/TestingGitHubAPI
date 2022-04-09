import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import user.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static final String GITHUB_USER_API_URL = "https://api.github.com/users/";

    public static void main(String[] args) throws IOException, InterruptedException {

        String userName;
        final String token = System.getenv("GITHUB_TOKEN");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter GitHub User: ");

        userName = scanner.next();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+token)
                .uri(URI.create(GITHUB_USER_API_URL+userName))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(parseJsonToUser(response));
    }

    // parse JSON into objects
    public static User parseJsonToUser(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return  mapper.readValue(response.body(), new TypeReference<User>(){});
    }
}
