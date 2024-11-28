package com.user.bankUserService.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.bankUserService.entity.Account;
import com.user.bankUserService.request.WithdrawRequest;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findByAccountNumber(UUID accountUUID);

  

}
