package com.dota.controllers;

import com.dota.model.AnalyzeRequest;
import com.dota.model.MatchDraft;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/draft")
public class DraftValidationController {

    private final Function<AnalyzeRequest, MatchDraft> draftMapper;

    public DraftValidationController(
            Function<AnalyzeRequest, MatchDraft> draftMapper) {
        this.draftMapper = draftMapper;
    }

    @PostMapping("/validate")
    public ValidationResponse validate(@RequestBody AnalyzeRequest request) {
        try {
            draftMapper.apply(request);
            return new ValidationResponse(true, List.of());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ValidationResponse(false, List.of(e.getMessage()));
        }
    }

    public record ValidationResponse(
            boolean valid,
            List<String> errors
    ) {
    }
}