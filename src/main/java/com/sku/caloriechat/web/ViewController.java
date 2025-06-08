package com.sku.caloriechat.web;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        Long   uid  = (Long)   session.getAttribute("LOGIN_USER_ID");
        String name = (String) session.getAttribute("LOGIN_USER_NAME");

        model.addAttribute("pageTitle", "홈");
        model.addAttribute("body",      "home");
        model.addAttribute("userId",    uid);         // JS 용
        model.addAttribute("userName",  name);        // 인사말 용

        return "layout/base";
    }

    /** 로그인 폼 */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "로그인");
        model.addAttribute("body", "login");
        return "layout/base";
    }

    /** 회원가입 폼 */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("body", "signup");
        return "layout/base";
    }


    /** 로그인 후 메인(대시보드) */
    @GetMapping("/home")
    public String dashboard(Model model, HttpSession session) {
        Long userId  = (Long) session.getAttribute("LOGIN_USER_ID");
        String name  = (String) session.getAttribute("LOGIN_USER_NAME"); // null 가능

        model.addAttribute("userId", userId);
        model.addAttribute("userName", name);
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("body", "home");
        return "layout/base";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}