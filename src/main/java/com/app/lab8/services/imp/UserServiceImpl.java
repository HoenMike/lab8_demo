package com.wadlab.academy_bank.services.impl;

import com.wadlab.academy_bank.dto.*;
import com.wadlab.academy_bank.entity.User;
import com.wadlab.academy_bank.repository.UserRepository;
import com.wadlab.academy_bank.services.EmailService;
import com.wadlab.academy_bank.services.UserService;
import com.wadlab.academy_bank.utils.AccountUtilis;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
// Check whether email exsit in database
        if (userRepository.existsByEmail(userRequest.getEmail())) {
// if exsit return BankResponse with response
            return BankResponse.builder()
                    .responseCode(AccountUtilis.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtilis.ACCOUNT_EXISTS_MESSAGE)
                    .AccountInfo(null)
                    .build();
        }
        // Using builder to create new User you call use contructor instead
        // User newUser = new User("Nghia", "Nguyen", "Trung", "123 Linh Trung"...)

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .sateOfOrigin(userRequest.getSateOfOrigin())
                .accountNumber(AccountUtilis.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .email(userRequest.getEmail())
                .status("ACTIVE")
                .build();
// Save user to database by repository
        User savedUser = userRepository.save(newUser);
// Create account Info
        AccountInfo accountInfo = AccountInfo.builder()
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .accountName(savedUser.getFirstName() + " "
                        + savedUser.getLastName() + " "
                        + savedUser.getOtherName())
                .build();
        return BankResponse.builder()
                .responseCode(AccountUtilis.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtilis.ACCOUNT_CREATION_SUCCESS_MASSAGE)
                .AccountInfo(accountInfo)
                .build();
    }
}
