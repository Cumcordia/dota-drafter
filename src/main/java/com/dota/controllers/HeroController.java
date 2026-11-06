package com.dota.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dota.draft.HeroFactory;
import com.dota.model.Hero;

@CrossOrigin
@RestController
@RequestMapping("/api/heroes")
public class HeroController {

    @GetMapping
    public List<Hero> getHeroes() {
        return HeroFactory.getHeroes();
    }
}