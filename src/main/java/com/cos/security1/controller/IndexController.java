package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //View를 리턴하겠다라는 것이다.
public class IndexController {

    @Autowired
    private UserRepository userRepository; //의존성 주입

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login ====================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : "+principalDetails.getUser());//getPrincipal은 리턴타입이 오브젝트이다.
        System.out.println("userDetails(PrincipalDetails) : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,@AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login ====================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : "+oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }
    // http://localhost:8080
    // http://localhost:8080/
    @GetMapping({"","/"})
    public String index(){
        // Mustache라는 것을 사용할 것이다.
        // Mustache의 가본폴더는 src/main/resources/
        // ViewResolver설정 templates를 Prefix로 잡고 .mustache를 Suffix로 잡는다. (yml에서 설정함)
        // Mustache를 사용하겠다고 설정을 하면 자동으로 경로가 위와같이 설정이 되기 때문에 yml에서 따로 설정x해도 된다.
        return "index"; //여기서 index파일은 view다.
    }

    // oAuth로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails로 받을 수 있다.
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        // @AuthenticationPrincipal 어노테이션으로 접근을 하면 바로 접근이 가능하다.
        System.out.println("PrincipalDetails : "+principalDetails.getUser());
        return "user";
    }

    // 시큐리티 설정
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    // 시큐리티 설정
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // Spring Security가 해당 주소를 낚아채버림
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    // 시큐리티 설정
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    // 시큐리티 설정
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);// 인코딩 완료해서 setPassword
        userRepository.save(user); //회원가입이 잘되지만 시큐리티로는 로그인을 할 수없다 이유는 패스워드 암호화가 되지 않았기 떄문
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 메소드에 권한을 간단하게 걸고 싶으면 이런식으로 구현한다.
    @GetMapping("/info")// 누구나 가진다.
    public @ResponseBody String info(){
        return "개인정보";
    }

    //data()라는 메서드가 실행되기 직전에 실행되는 것 - 권한 체크
    //내가 여러개를 걸고 싶으면 PreAuthorize 에서 or 로 여러개 걸면 된다.
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")// 누구나 가진다.
    public @ResponseBody String data(){
        return "데이터 페이지";
    }
}
