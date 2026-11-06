package com.dota.analysis;

import com.dota.controllers.AnalyzeRequestMapper;
import com.dota.laning.LaningAnalyzer;
import com.dota.model.AnalyzeRequest;
import com.dota.model.MatchDraft;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AnalysisConfig {

    @Bean
    public LaningAnalyzer laningAnalyzer() {
        return new LaningAnalyzer();
    }

    @Bean
    public AnalysisFacade analysisFacade(LaningAnalyzer laningAnalyzer) {
        return new AnalysisFacade(
                laningAnalyzer::analyze,
                new MidgameStrategy(),
                new LategameStrategy()
        );
    }

    @Bean
    public Function<AnalyzeRequest, MatchDraft> draftMapper() {
        return new AnalyzeRequestMapper();
    }
}