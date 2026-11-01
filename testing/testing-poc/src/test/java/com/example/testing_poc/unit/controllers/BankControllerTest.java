package com.example.testing_poc.unit.controllers;

import com.example.testing_poc.dtos.AccountDto;
import com.example.testing_poc.dtos.ReferentielDto;
import com.example.testing_poc.services.BankService;
import com.example.testing_poc.controllers.BankController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;

import javax.security.auth.login.AccountNotFoundException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankService bankService;

    // Resource getReferentiels

    // Happy Path

    @Test
    void shouldReturnReferentiels_whenIsValid() throws Exception {

        // Arrange
        String type = "CURRENCY";

        ReferentielDto mad = new ReferentielDto();
        mad.setCode("MAD");
        mad.setLibelle("Dirham marocain");
        mad.setDescription("Monnaie marocaine");

        when(bankService.getReferentiels(type))
                .thenReturn(List.of(mad));

        // Act & Assert
        mockMvc.perform(get("/bank/referentiels/{type}", type))
                // HTTP
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Structure
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                // Premier élément
                .andExpect(jsonPath("$[0].code").value("MAD"))
                .andExpect(jsonPath("$[0].code").isString())
                .andExpect(jsonPath("$[0].libelle").isString())
                .andExpect(jsonPath("$[0].libelle").value("Dirham marocain"))
                .andExpect(jsonPath("$[0].description").isString())
                .andExpect(jsonPath("$[0].description").value("Monnaie marocaine"));
        // Vérification interfaçage
        verify(bankService).getReferentiels(type);
    }

    // Negative Tests

    @Test
    void shouldReturnNotFound_whenTypeIsMissing() throws Exception {

        mockMvc.perform(get("/referentiels"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(bankService);
    }

    // Resource getAccount

    // Happy Path

    @Test
    void shouldReturnAccount_whenIdIsValid() throws Exception {

        // Arrange
        Long id = 123L;

        AccountDto account = new AccountDto();
        account.setId(id);
        account.setAccountNumber("123456789");
        account.setBalance(BigDecimal.valueOf(1000));

        when(bankService.getAccount(id)).thenReturn(account);

        // Act & Assert
        mockMvc.perform(get("/bank/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.accountNumber").isString())
                .andExpect(jsonPath("$.accountNumber").value("123456789"))
                .andExpect(jsonPath("$.balance").isNumber())
                .andExpect(jsonPath("$.balance").value(1000));

        verify(bankService).getAccount(id);
    }

    // Negative Tests

    @Test
    void shouldReturnBadRequest_whenIdIsNotNumeric() throws Exception {

        mockMvc.perform(get("/bank/accounts/ABC"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bankService);
    }

    @Test
    void shouldReturnNotFound_whenIdIsMissing() throws Exception {

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(bankService);
    }

    @Test
    void shouldReturnNotFound_whenAccountDoesNotExist() throws Exception {

        Long id = 999L;

        when(bankService.getAccount(id))
                .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isNotFound());

        verify(bankService).getAccount(id);
    }


}