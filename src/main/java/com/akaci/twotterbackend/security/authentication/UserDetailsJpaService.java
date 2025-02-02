package com.akaci.twotterbackend.security.authentication;

import com.akaci.twotterbackend.persistence.entity.AccountEntity;
import com.akaci.twotterbackend.persistence.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsJpaService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserDetailsJpaService.class);
    private final AccountRepository accountRepository;

    public UserDetailsJpaService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AccountEntity> opAccount = accountRepository.findByUsername(username);
        if (opAccount.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        AccountEntity account = opAccount.get();

        String hashedPassword = account.getPassword();
        GrantedAuthority authority = new GrantedAuthorityImpl(account.getRole().getRole());

        return User.builder()
                .username(username)
                .password(hashedPassword)
                .authorities(authority)
                .build();
    }
}
