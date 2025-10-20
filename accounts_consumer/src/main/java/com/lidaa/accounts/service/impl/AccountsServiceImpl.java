package com.lidaa.accounts.service.impl;

import com.lidaa.accounts.client.AccountsClient;
import com.lidaa.accounts.constants.AccountsConstants;
import com.lidaa.accounts.dto.AccountsDto;
import com.lidaa.accounts.dto.CustomerDto;
import com.lidaa.accounts.dto.ResponseDto;
import com.lidaa.accounts.entity.Accounts;
import com.lidaa.accounts.entity.Customer;
import com.lidaa.accounts.exception.ResourceNotFoundException;
import com.lidaa.accounts.mapper.CustomerMapper;
import com.lidaa.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private final AccountsClient accountsClient;

    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public ResponseEntity<ResponseDto> createAccount(CustomerDto customerDto) {
        return accountsClient.createAccount(customerDto);
    }


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public ResponseEntity<CustomerDto>  fetchAccount(String mobileNumber) {
      return   accountsClient.fetchAccountDetails(mobileNumber);
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public ResponseEntity<ResponseDto> updateAccount(CustomerDto customerDto) {
       return accountsClient.updateAccountDetails(customerDto);
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public ResponseEntity<ResponseDto> deleteAccount(String mobileNumber) {
    return accountsClient.deleteAccountDetails(mobileNumber);
    }


}
