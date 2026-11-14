package com.example.services;

import com.example.dtos.*;
import java.util.List;

public interface BankService {

    List<ReferentielDto> getReferentiels(String type);

    AccountDto getAccount(Long id);

    AccountDto createAccount(CreateAccountRequest request);

    CreditCalculationResponse calculateCredit(CreditCalculationRequest request);

    TransferResponse transfer(TransferRequest request);

    void deleteAccount(Long id);
}
