package com.example.pr_creation.service;

import com.example.pr_creation.dto.AddComment;
import com.example.pr_creation.dto.ChangedFile;
import com.example.pr_creation.dto.GitHubClient;
import com.example.pr_creation.dto.GitHubPRPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private  GitHubClient githubClient;

    @Value("${github.token}")
    private String githubToken;

//    public ReviewService(GitHubClient githubClient) {
//        this.githubClient = githubClient;
//    }

//    public ReviewService(GitHubClient githubClient) {
//        this.githubClient = githubClient;
//    }

    public void enqueue(String owner, String repo, int prNumber, String headSha, String action) {
        String token = "Bearer " + githubToken;

        GitHubPRPayload pr = githubClient.getPullRequest(owner, repo, prNumber, token);

        List<ChangedFile> files = githubClient.getChangedFiles(owner, repo, prNumber, token);
        if (!files.isEmpty()) {
            for (ChangedFile file : files) {
                System.out.println(file.getFilename());
                System.out.println(file.getChanges());
                System.out.println(file.getPatch());
                System.out.println(file.getDeletions());
                System.out.println(file.getStatus());
                System.out.println(file.getAdditions());
                System.out.println("----------------------------------------------------------->");
            }
        }


        // Example validation logic
        if (pr.getTitle() == null || pr.getTitle().isBlank()) {
            System.out.println("❌ PR title is missing");
        } else {
            System.out.println("✅ PR title: " + pr.getTitle());
            if(pr.getTitle().contains("test")){
                AddComment comment = new AddComment();
                comment.setBody("Avoid using naked string literals here. Please extract it into a [private static final] constant or use an enum. This improves readability, avoids duplication, and makes future maintenance easier.\n" + " ");
                AddComment addComment = githubClient.addcomment(owner, repo, prNumber, comment, token);
            }
            if(pr.getTitle().contains("Checking")){
                AddComment comment = new AddComment();
                comment.setBody("Final variables should use UPPER_SNAKE_CASE to follow Java standards.\n" + " ");
                AddComment addComment = githubClient.addcomment(owner, repo, prNumber, comment, token);
            }
            System.out.println("PR state: " + pr.getState());
            System.out.println("PR Body: " + pr.getBody());
        }


        // Add more validations here
    }
}

