package dota.dota.analysis;

import dota.dota.analysis.*;
import dota.dota.laning.LaningAnalyzer;
import dota.dota.model.AnalyzeRequest;
import dota.dota.model.MatchDraft;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisConfig {

    @Bean
    public AnalysisFacade analysisFacade(LaningAnalyzer laningAnalyzer) {
        return new AnalysisFacade(laningAnalyzer::analyze,
                new MidgameStrategy(), new LategameStrategy());
    }

    /** TODO(A): заменить на вызов DraftBuilder (heroId -> Hero, сборка, валидация). */
    @Bean
    public Function<AnalyzeRequest, MatchDraft> draftMapper() {
        return req -> { throw new UnsupportedOperationException("draftMapper: ждём DraftBuilder от A"); };
    }
}