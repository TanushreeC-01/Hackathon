package com.hackathon.hackathon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HackathonController {

        // GET endpoint
        @GetMapping("/hello")
        public String sayHello() {
            return "Hello World";
        }

        // POST endpoint
        @PostMapping("/hello")
        public String postHello(@RequestBody(required = false) String input) {
            return "Hello World" ;
        }

    @PostMapping(value = "/github", consumes = "application/json")
    public ResponseEntity<Map<String,Object>> onGitHub(
            @RequestHeader(value="X-GitHub-Event", required=false) String event,
            @RequestHeader(value="X-GitHub-Delivery", required=false) String deliveryId,
            @RequestBody Map<String,Object> payload // for quick start; see raw-body section below
    ) {
        // 1) Basic logging
        //testing
        System.out.println("GH delivery=" + deliveryId + " event=" + event);

        return ResponseEntity.ok(Map.of("ok", true));
    }
    }
