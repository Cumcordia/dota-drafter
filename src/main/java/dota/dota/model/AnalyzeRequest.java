package dota.dota.model;

import java.util.List;

public record AnalyzeRequest(List<HeroPick> radiant, List<HeroPick> dire) {
    public record HeroPick(String heroId, int position, String lane) {}
}