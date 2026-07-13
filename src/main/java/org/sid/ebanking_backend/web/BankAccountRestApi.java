package org.sid.ebanking_backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebanking_backend.dtos.*;
import org.sid.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.sid.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.ebanking_backend.services.BankAccountService;
import org.sid.ebanking_backend.services.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountRestApi {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam (name="page",defaultValue = "0") int page,
                                               @RequestParam (name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page,size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit( @RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer( @RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException  {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }



}

