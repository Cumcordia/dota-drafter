package com.dota.analysis;

import com.dota.controllers.AnalyzeRequestMapper;
import com.dota.laning.LaningAnalyzer;
import com.dota.model.AnalyzeRequest;
import com.dota.model.MatchDraft;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisConfig {

    @Bean
    public AnalysisFacade analysisFacade() {
        return new AnalysisFacade(new LaningAnalyzer()::analyze,
                new MidgameStrategy(), new LategameStrategy());
    }

    @Bean
    public Function<AnalyzeRequest, MatchDraft> draftMapper() {
        return new AnalyzeRequestMapper();
    }
}