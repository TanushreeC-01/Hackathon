package com.example.pr_creation.controller;

import com.example.pr_creation.service.OpenAIService;
import com.example.pr_creation.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired
    private  ReviewService reviewService;

    @Autowired
    private OpenAIService openAIService;

//    public WebhookController(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }

    @PostMapping(value = "/github", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> onGitHub(
            @RequestHeader(value = "X-GitHub-Event", required = false) String event,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestBody Map<String, Object> payload
    ) {
        Map<String, Object> pr = (Map<String, Object>) payload.get("pull_request");
        Map<String, Object> repo = (Map<String, Object>) payload.get("repository");

        String owner = ((Map<String, Object>) repo.get("owner")).get("login").toString();
        String name = repo.get("name").toString();
        int prNumber = ((Number) pr.get("number")).intValue();
        String action = payload.get("action").toString();
        String headSha = ((Map<String, Object>) pr.get("head")).get("sha").toString();

        reviewService.enqueue(owner, name, prNumber, headSha, action);

        String prompt = "Review this GitHub Pull Request:\n"
                + "Owner: " + ((Map<String, Object>) repo.get("owner")).get("login").toString() + "\n"
                + "name: " + repo.get("name").toString() + "\n"
                + "prNumber: " + ((Number) pr.get("number")).intValue()+ "\n"
                + "action: " + payload.get("action").toString()+ "\n"
                + "headSha: " +((Map<String, Object>) pr.get("head")).get("sha").toString()+ "\n" ;
        String feedback = openAIService.validatePRWithAI(prompt);

        System.out.println(feedback);

        return ResponseEntity.ok(Map.of("ok", true));
    }

}
