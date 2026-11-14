package com.example.unit.controllers;

import com.example.dtos.AccountDto;
import com.example.dtos.ReferentielDto;
import com.example.excetions.AccountNotFoundException;
import com.example.services.BankService;
import com.example.controllers.BankController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;

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

    // Exception Handling Test

    @Test
    void shouldReturnNotFound_whenAccountDoesNotExist() throws Exception {

        // Arrange
        Long id = 999L;

        when(bankService.getAccount(id))
                .thenThrow(new AccountNotFoundException(id));

        // Act & Assert
        mockMvc.perform(get("/bank/accounts/{id}", id)
                        .header("Accept-Language", "en"))

                // HTTP Status
                .andExpect(status().isNotFound())
                // Response Headers
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Response Body - Error Contract
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"));

        // Verify Service Interaction
        verify(bankService).getAccount(id);
    }

}