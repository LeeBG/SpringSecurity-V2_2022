package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//CRUD함수를 JpaRepository가 들고 있다.
//@Repository라는 어노테이션이 없어도 IoC된다. 이유는 JpaRepository를 상속했기 떄문에 자동으로 Bean으로등록
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    //findBy -> 규칙이고 Username은 문법이다.
    // select * from user where username = 1?
    public User findByUsername(String username);// JPA QueryMethod를 사용해여한다.
}
