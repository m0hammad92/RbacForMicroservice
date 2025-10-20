package com.lidaa.accounts.service;

import com.lidaa.accounts.dto.CustomerDto;
import com.lidaa.accounts.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface IAccountsService {

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    ResponseEntity<ResponseDto> createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    ResponseEntity<CustomerDto>  fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    ResponseEntity<ResponseDto> updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    ResponseEntity<ResponseDto> deleteAccount(String mobileNumber);


}
