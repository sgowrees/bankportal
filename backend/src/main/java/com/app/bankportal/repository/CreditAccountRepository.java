package com.app.bankportal.repository;

import com.app.bankportal.model.CreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreditAccountRepository extends JpaRepository<CreditAccount, Long> {
    List<CreditAccount> findByUserId(Long userId);
}