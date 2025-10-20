package com.lidaa.accounts.mapper;

import com.lidaa.accounts.dto.AccountsDto;
import com.lidaa.accounts.entity.Accounts;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T15:59:41+0530",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class AccountsMapperImpl implements AccountsMapper {

    @Override
    public AccountsDto mapToAccountsDto(Accounts accounts) {
        if ( accounts == null ) {
            return null;
        }

        AccountsDto accountsDto = new AccountsDto();

        accountsDto.setAccountNumber( accounts.getAccountNumber() );
        accountsDto.setAccountType( accounts.getAccountType() );
        accountsDto.setBranchAddress( accounts.getBranchAddress() );

        return accountsDto;
    }

    @Override
    public Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
        if ( accountsDto == null ) {
            return accounts;
        }

        accounts.setAccountNumber( accountsDto.getAccountNumber() );
        accounts.setAccountType( accountsDto.getAccountType() );
        accounts.setBranchAddress( accountsDto.getBranchAddress() );

        return accounts;
    }
}
