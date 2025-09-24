package com.example.pr_creation.dto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "github", url = "${github.api-url}")
public interface GitHubClient {

    @GetMapping("/repos/{owner}/{repo}/pulls/{pull_number}")
    GitHubPRPayload getPullRequest(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("pull_number") int pullNumber,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/repos/{owner}/{repo}/pulls/{pull_number}/files")
    List<ChangedFile> getChangedFiles(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("pull_number") int pullNumber,
            @RequestHeader("Authorization") String token
    );

}
