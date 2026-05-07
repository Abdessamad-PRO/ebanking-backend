package org.sid.ebanking_backend.services;

import org.sid.ebanking_backend.entities.BankAccount;
import org.sid.ebanking_backend.entities.CurrentAccount;
import org.sid.ebanking_backend.entities.Customer;
import org.sid.ebanking_backend.entities.SavingAccount;
import org.sid.ebanking_backend.repositories.BankAccountRepository;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId );

    SavingAccount saveSavingBankAccount(double intialBalance, double interestRate, Long customerId );

    List<Customer> listCustomer();

    BankAccount getBankAccount(String accountId);

    void debit(String accountId,double amount, String description);

    void credit(String accountId,double amount, String description);

    void transfer(String accountIdSource, String AccountIdDestination, double amount);

}
