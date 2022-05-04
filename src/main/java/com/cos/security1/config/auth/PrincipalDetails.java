package com.cos.security1.config.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 sesion을 만들어줍니다. (Security ContextHolder) - 키값에다가 세션정보를 저장
// 오브젝트가 정해져있다. -> Authentication타입 객체
// Authentication 객체 안에 User정보가 있어야 된다.
// User 오브젝트타입 => UserDetails타입 객체가 있어야한다.

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session에다가 세션정보를 저장해주는데 여기에 들어갈 수 있는 객체가 Authentication객체인데 여기에 User정보를 저장할 때,
// UserDetail타입을 꺼내면 User정보를 알 수 있다.
public class PrincipalDetails implements UserDetails {

    // 우리 유저정보는 User객체가 들고있다.
    private User user;  // 콤포지션

    public PrincipalDetails(User user){
        this.user=user;
    }

    // 해당 유저의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { //계정이 활성화
        // 우리사이트에서 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 함
        // 현재시간 - 로그인 시간 -> 1년을 초과하면 return false; 이런식으로 구현을 하면 된다.
        return true;
    }
}
