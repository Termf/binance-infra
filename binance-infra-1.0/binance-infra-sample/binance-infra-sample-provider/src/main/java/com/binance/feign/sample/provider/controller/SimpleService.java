package com.binance.feign.sample.provider.controller;

import java.util.List;

import feign.Param;
import feign.RequestLine;

public class SimpleService {

    public static class Contributor {
        String login;
        int contributions;
    }

    public static class Issue {
        String title;
        String body;
        List<String> assignees;
        int milestone;
        List<String> labels;
    }

    interface GitHub {
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);

        @RequestLine("POST /repos/{owner}/{repo}/issues")
        void createIssue(Issue issue, @Param("owner") String owner, @Param("repo") String repo);

    }
}
