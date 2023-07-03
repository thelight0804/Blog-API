package com.gdsc.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gdsc.blog.user.Member;
import com.gdsc.blog.user.MemberRepository;
import com.gdsc.blog.user.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Profile("test")
class BlogApplicationTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSignup() throws Exception {
        String username = "test"; //set username
        String password = "test123"; //set password

        Member member = memberService.join(username, password); //sign up

        assertEquals(username, member.getUsername());
        //not use assertEquals to password
        assertTrue(passwordEncoder.matches(password, member.getPassword()));
    }

    @Test
    public void testLogin() throws Exception{
        //normar member login
        String username1 = "test";
        String password1 = "test123";

        //worng member login
        String username2 = "test2";
        String password2 = "test1234";


        //test signup
        Member member = memberService.join(username1, password1);
        assertEquals(username1, member.getUsername());
        assertTrue(passwordEncoder.matches(password1, member.getPassword()));

        //test normal login
        try{
            Member getMember = memberService.login(username1, password1);
            System.out.println("getMember.getId()= " + getMember.getId());
            System.out.println("getMember.getUsername() = " + getMember.getUsername());
            System.out.println("getMember.getPassword() = " + getMember.getPassword());
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong id or password", e.getMessage());
        }

        //test wrong login
        try {
            Member getMember = memberService.login(username2, password2);
            System.out.println("getMember.getId()= " + getMember.getId());
            System.out.println("getMember.getUsername() = " + getMember.getUsername());
            System.out.println("getMember.getPassword() = " + getMember.getPassword());
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong id or password", e.getMessage());
        }
    }

    @Test
    public void testCreateJwtToken() throws Exception {
        String username = "test";
        String password = "test123";

        //signup
        Member member = memberService.join(username, password);

        //login
        try{
            Member getMember = memberService.login(username, password);
        }catch(IllegalArgumentException e){
            assertEquals("Wrong id or password", e.getMessage());
        }

        //create token
        JwtToken jwtToken = new JwtToken(); //create jwt token object
        String token = jwtToken.createJwtToken(member); //create jwt token
        System.out.println("token = " + token); //print jwt token
    }
}
