package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.auth.provider.FacebookUserInfo;
import com.cos.security1.config.auth.provider.GoogleUserInfo;
import com.cos.security1.config.auth.provider.NaverUserInfo;
import com.cos.security1.config.auth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> 코드를 리턴 (OAuth2 라이브러리) -> AccessToken요청
// userRuest 정보 -> loadUser()함수 호출 -> 구글로부터 회원프로필을 받아준다.

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 UserRequest 데이터에 대한 후처리가 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : "+userRequest.getClientRegistration());// UserRequest를 출력
        System.out.println("getAccessToken : "+userRequest.getAccessToken().getTokenValue());// UserRequest를 출력
//        System.out.println("getAttributes : "+super.loadUser(userRequest).getAttributes());// return되는 super.loadUser(userRequest).getAttributes()출력

        // 여기서 받은 정보를 토대로
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("getAttribute" + oauth2User.getAttributes());

        OAuth2UserInfo oauth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oauth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }else{
            System.out.println("구글과 페이스북,네이버 oAuth만 지원한다.");
        }

        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderID();
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("password");
        String email = oauth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        // OAuth로 회원가입이 되어있지 않은 상태에서 회원가입을 강제로 진행
        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        //회원가입을 강제로 진행할 예정
        return new PrincipalDetails(userEntity,oauth2User.getAttributes());
    }
}
