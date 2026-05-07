package org.sid.ebanking_backend.services;

import org.sid.ebanking_backend.entities.BankAccount;
import org.sid.ebanking_backend.entities.CurrentAccount;
import org.sid.ebanking_backend.entities.Customer;
import org.sid.ebanking_backend.entities.SavingAccount;
import org.sid.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.sid.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.ebanking_backend.repositories.BankAccountRepository;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId ) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double intialBalance, double interestRate, Long customerId ) throws CustomerNotFoundException ;

    List<Customer> listCustomer();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException ;

    void debit(String accountId,double amount, String description) throws BalanceNotSufficientException,BankAccountNotFoundException ;

    void credit(String accountId,double amount, String description) throws BalanceNotSufficientException,BankAccountNotFoundException ;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();
}
