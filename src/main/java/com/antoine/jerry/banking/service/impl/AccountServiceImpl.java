package com.antoine.jerry.banking.service.impl;

import com.antoine.jerry.banking.dto.AccountDto;
import com.antoine.jerry.banking.entity.Account;
import com.antoine.jerry.banking.mapper.AccountMapper;
import com.antoine.jerry.banking.repository.AccountRepository;
import com.antoine.jerry.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        //first check if the id exist
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));
        //get the balance and add it to the new amount deposited
        double total = account.getBalance() + amount;
        account.setBalance(total);
        //save the total in account database
        Account savedAccount = accountRepository.save(account);
        //convert account jpa entity into accountDTO
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        //first check if the id exist
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        //throw exception if the account balance is less than the amount to be withdrawn
        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient amount");
        }
        //get the balance and subtract the amount to be withdrawn
        double total = account.getBalance() - amount;
        account.setBalance(total);
        //save the total in account database
        Account savedAccount = accountRepository.save(account);
        //convert account jpa entity into accountDTO
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        //first check if the id exist
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        accountRepository.deleteById(id);

    }
}
