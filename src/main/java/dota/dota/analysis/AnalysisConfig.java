package dota.dota.analysis;

import dota.dota.draft.LaningAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisConfig {
    @Bean
    public AnalysisFacade analysisFacade(LaningAnalyzer laningAnalyzer) {
        return new AnalysisFacade(laningAnalyzer::analyze,
                new MidgameStrategy(), new LategameStrategy());
    }
}