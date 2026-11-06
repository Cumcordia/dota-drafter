package com.dota.controllers;

import java.util.Map;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.dota.analysis.AnalysisFacade;
import com.dota.model.*;

@RestController
@RequestMapping("/api/analyze")
public class AnalysisController {
    private final AnalysisFacade facade;
    private final Function<AnalyzeRequest, MatchDraft> draftMapper;

    public AnalysisController(AnalysisFacade facade, Function<AnalyzeRequest, MatchDraft> draftMapper) {
        this.facade = facade;
        this.draftMapper = draftMapper;
    }

    @PostMapping
    public AnalysisResponse analyze(@RequestBody AnalyzeRequest request) {
        return facade.analyze(draftMapper.apply(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }
}