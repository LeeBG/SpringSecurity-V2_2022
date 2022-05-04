package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessUrl("/login")으로 걸어놨기 때문에
// /login요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행

@Service // PrincipalDetailsService가 IoC에 등록이 된다.
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    // 시큐리티 세션에=> Authentication => UserDetails(PrincipalDetails)타입
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        //UserDetails가 리턴이 되면 리턴된 UserDetails값이 Authentication내부에 쏙 들어간다. 그리고
        // 시큐리티 Session내부에 Authentication 객체가 들어가고
        // Authentication객체의 내부에는 UserDetails 리턴
        return null;
    }
}
