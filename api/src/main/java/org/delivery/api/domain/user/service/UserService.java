package org.delivery.api.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.UserErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserUpdateRequest;
import org.delivery.api.domain.user.model.User;
import org.delivery.db.user.UserEntity;
import org.delivery.db.user.UserRepository;
import org.delivery.db.user.enums.UserRole;
import org.delivery.db.user.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity register(UserEntity userEntity){
        var email = userEntity.getEmail();
        var emailCheck = userRepository.findByEmail(email);
        if(!emailCheck.isEmpty()) throw new ApiException(ErrorCode.BAD_REQUEST);

        return Optional.ofNullable(userEntity)
                .map(it->{
                    userEntity.setRole(UserRole.USER);
                    userEntity.setStatus(UserStatus.REGISTERED);
                    userEntity.setRegisteredAt(LocalDateTime.now());
                    return userRepository.save(userEntity);
                }).orElseThrow(()->new ApiException(ErrorCode.NULL_POINT,"User Entity Null"));
    }

    public UserEntity login(UserLoginRequest request){
        var user = getUserWithThrow(request.getEmail(), request.getPassword());
        return user;
    }

    public UserEntity getUserWithThrow(String email, String password){
        return userRepository.findFirstByEmailAndPasswordAndStatusOrderByIdDesc(email,password,UserStatus.REGISTERED)
                .orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getUserWithThrow(Long userId){
        return userRepository.findFirstByIdAndStatusOrderByIdDesc(userId,UserStatus.REGISTERED)
                .orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public UserEntity update(Long userId, UserUpdateRequest request){
        UserEntity user = getUserWithThrow(userId);
        System.out.println(user.getPassword());
        System.out.println(request.getPassword());
        if(!user.getPassword().equals(request.getPassword())) throw new ApiException(UserErrorCode.PASSWORD_ERROR);

        user.setAddress(request.getAddress());
        user.setName(request.getName());
        return user;
    }
}
