package com.salon.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Locale;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("module","index");
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("module", "login");
        return "login";
    }

    @GetMapping("/registration")
    public String regPage(Model model){
        model.addAttribute("module", "registration");
        return "registration";
    }

    @GetMapping("/masters")
    public String mastersPage(Model model, Locale locale){
        model.addAttribute("module", "masters");
        model.addAttribute("locale", locale);
        return "masters";
    }

    @GetMapping("/create_app/{id}")
    public String addAppointmentPage(@PathVariable Long id,
                                     Model model, Locale locale){
        model.addAttribute("masterId",id);
        model.addAttribute("locale", locale);
        return "create_app";
    }

    @GetMapping("/me/appointments")
    public String addMyAppPage(Model model, Locale locale){
        model.addAttribute("module","my_account");
        model.addAttribute("locale", locale);
        return "user_apps";
    }

    @GetMapping("/all_appointments")
    public String allAppointmentsPage(Model model, Locale locale){
        model.addAttribute("module","all_appointments");
        model.addAttribute("locale", locale);
        return "all_appointments";
    }

    @GetMapping("/create_master")
    public String addMasterPage(Model model, Locale locale){
        model.addAttribute("module","create_master");
        model.addAttribute("locale", locale);
        return "create_master";
    }

    @GetMapping("/all_masters")
    public String allMastersPage(Model model, Locale locale){
        model.addAttribute("module","all_masters");
        model.addAttribute("locale", locale);
        return "all_masters";
    }

    @GetMapping("/me/account")
    public String accountPage(Model model, Locale locale){
        model.addAttribute("module","my_account");
        model.addAttribute("locale", locale);
        return "userAccount";
    }
}
