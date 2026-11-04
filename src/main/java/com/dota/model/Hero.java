package com.dota.model;

public record Hero(
        String id,
        String name,
        int position,
        double lanePower,
        double tempo,
        double greed,
        double scaling,
        double teamfight,
        double push
) {
}