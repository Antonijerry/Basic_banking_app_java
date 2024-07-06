package com.antoine.jerry.banking.repository;

import com.antoine.jerry.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
