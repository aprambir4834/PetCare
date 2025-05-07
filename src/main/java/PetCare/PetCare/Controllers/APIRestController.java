/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PetCare.PetCare.Controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RestController
public class APIRestController {
     @PostMapping("/ask")
    public ResponseEntity<String> askCopilot(@RequestBody Map<String, String> body) {
        String message = body.get("message");

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://copilot5.p.rapidapi.com/copilot"))
                    .header("Content-Type", "application/json")
                    .header("x-rapidapi-host", "copilot5.p.rapidapi.com")
                    .header("x-rapidapi-key", "c2e2e64348msh80e8bd3ccbf40e4p104d64jsn4644fdcaac09")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"message\":\"" + message + "\",\"conversation_id\":null,\"markdown\":true}"
                    ))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.ok(response.body());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/pets")
    public ResponseEntity<Object> getPetsNews() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://real-time-news-data.p.rapidapi.com/topic-news-by-section?topic=ENTERTAINMENT&section=CAQiSkNCQVNNUW9JTDIwdk1EZGpNWFlTQldWdUxVZENHZ0pKVENJT0NBUWFDZ29JTDIwdk1ETnliSFFxQ2hJSUwyMHZNRE55YkhRb0FBKi4IACoqCAoiJENCQVNGUW9JTDIwdk1EZGpNWFlTQldWdUxVZENHZ0pKVENnQVABUAE&limit=10&country=US&lang=en"))
                    .header("X-RapidAPI-Key", "c2e2e64348msh80e8bd3ccbf40e4p104d64jsn4644fdcaac09")
                    .header("X-RapidAPI-Host", "real-time-news-data.p.rapidapi.com")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok().body(response.body());
            } else {
                return ResponseEntity.status(response.statusCode()).body("Error fetching news: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching news");
        }
    }
}
