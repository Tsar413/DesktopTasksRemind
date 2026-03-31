package com.study.workToDo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/work")
public class VIewController {

    @GetMapping()
    public ModelAndView showAHtml() {
        return new ModelAndView("index","time1",0);
    }

}
