package com.dota.controllers;

import com.dota.draft.HeroFactory;
import com.dota.model.Hero;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/heroes")
public class HeroController {

    @GetMapping
    public List<Hero> getHeroes() {
        return HeroFactory.getHeroes();
    }
}