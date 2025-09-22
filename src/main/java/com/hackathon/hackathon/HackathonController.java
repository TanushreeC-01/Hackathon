package com.hackathon.hackathon;

import org.springframework.web.bind.annotation.*;

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

    }
