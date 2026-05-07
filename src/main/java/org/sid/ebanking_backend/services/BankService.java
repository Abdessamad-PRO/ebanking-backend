package org.sid.ebanking_backend.services;

import jakarta.transaction.Transactional;
import org.sid.ebanking_backend.entities.BankAccount;
import org.sid.ebanking_backend.entities.CurrentAccount;
import org.sid.ebanking_backend.entities.SavingAccount;
import org.sid.ebanking_backend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("2c7ffbe4-cd48-42d9-8422-9e619cf6c42d").orElse(null);
        System.out.println("==============================");
        System.out.println(bankAccount.getId());
        System.out.println(bankAccount.getBalance());
        System.out.println(bankAccount.getStatus());
        System.out.println(bankAccount.getCreatedAt());
        System.out.println(bankAccount.getCustomer().getName());
        if(bankAccount instanceof CurrentAccount){
            System.out.println("Over draftc=:"+((CurrentAccount)bankAccount).getOverDraft());
        }else if(bankAccount instanceof SavingAccount){
            System.out.println("Rate =:"+((SavingAccount)bankAccount).getInterestRate());
        }
        bankAccount.getAccountOperations().forEach(op->{
            System.out.println(op.getType()+"\t"+op.getOperationDate()+"\t"+op.getAmount());
        });
    }
}
