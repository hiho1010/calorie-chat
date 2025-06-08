package com.sku.caloriechat.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /** 홈 */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "홈");
        model.addAttribute("body", "home");     // templates/home.html
        return "layout/base";                   // templates/layout/base.html
    }

    /** 로그인 폼 */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "로그인");
        model.addAttribute("body", "login");    // templates/login.html
        return "layout/base";
    }

    /** 회원가입 폼 */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("body", "signup");   // templates/signup.html
        return "layout/base";
    }
}