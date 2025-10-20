package com.lidaa.accounts.mapper;

import com.lidaa.accounts.dto.AccountsDto;
import com.lidaa.accounts.entity.Accounts;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AccountsMapper {

    public  AccountsDto mapToAccountsDto(Accounts accounts) ;

    public  Accounts mapToAccounts(AccountsDto accountsDto, @MappingTarget Accounts accounts);

}
