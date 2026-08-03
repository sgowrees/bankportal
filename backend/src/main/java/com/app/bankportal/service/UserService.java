package com.app.bankportal.service;

import com.app.bankportal.dto.LoginRequest;
import com.app.bankportal.dto.SignupRequest;
import com.app.bankportal.model.Account;
import com.app.bankportal.model.AccountType;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.AccountRepository;
import com.app.bankportal.repository.UserRepository;
import com.app.bankportal.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User signup(SignupRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        Account account = new Account();

        account.setBalance(BigDecimal.ZERO);
        account.setDailylimit(BigDecimal.valueOf(1000));
        account.setDailySpent(BigDecimal.ZERO);
        account.setUser(user);
        account.setAccountType(AccountType.CHECKING);
        account.setDefault(true);

        accountRepository.save(account);

        account.setAccountNumber("ACC-" + account.getId());

        accountRepository.save(account);

        return user;
    }

    public String login(LoginRequest request) {

        Optional<User> user = userRepository.findByUsername(
                request.getUsername()
        );

        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        User userFound = user.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                userFound.getPassword()
        )) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtService.generateToken(
                userFound.getId(),
                userFound.getUsername()
        );
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
    }
}