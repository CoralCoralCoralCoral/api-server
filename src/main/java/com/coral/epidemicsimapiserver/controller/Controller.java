package com.coral.epidemicsimapiserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/index.html");
    }
}
