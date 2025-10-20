package com.lidaa.accounts.client;


import com.lidaa.accounts.dto.CustomerDto;
import com.lidaa.accounts.dto.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface AccountsClient {

    @PostExchange("/create")
    ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto);

    @GetExchange("/fetch")
    ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber);

    @PutExchange("/update")
    ResponseEntity<ResponseDto> updateAccountDetails( @RequestBody CustomerDto customerDto);

    @DeleteExchange("/delete")
    ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam String mobileNumber);



}



