package com.example.OAuthJWT.service;

import com.example.OAuthJWT.dto.CustomOAuth2User;
import com.example.OAuthJWT.dto.NaverResponse;
import com.example.OAuthJWT.dto.OAuth2Response;
import com.example.OAuthJWT.dto.UserDTO;
import com.example.OAuthJWT.entity.UserEntity;
import com.example.OAuthJWT.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//리소스 서버로 부터 받은 토큰 처리
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // useRequest 에서 유저 값 가져오기
        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //registrationId : naver, kakao등
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
            //naver 이면 naver dto 처리

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디 값을 만듦.
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        if(existData == null){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_user");



            return new CustomOAuth2User(userDTO);
        }
        else{

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);


        }







    }

}
