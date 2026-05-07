package org.sid.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebanking_backend.entities.*;
import org.sid.ebanking_backend.enums.OperationType;
import org.sid.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.sid.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.ebanking_backend.repositories.AccountOperationRepository;
import org.sid.ebanking_backend.repositories.BankAccountRepository;
import org.sid.ebanking_backend.repositories.CustomerRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double intialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(intialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double intialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(intialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;

    }


    @Override
    public List<Customer> listCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bankccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException,BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not Sufficient");
        AccountOperation accountOperation= new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BalanceNotSufficientException,BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation= new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
        credit(accountIdSource,amount,"Tranfer from"+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }

}
