package com.northgatevologda.smartbudget.application.service.account.mapper;

import com.northgatevologda.smartbudget.application.service.account.dto.AccountDTO;
import com.northgatevologda.smartbudget.domain.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is responsible for mapping Account entities to AccountDTOs.
 */
@Component
public class AccountServiceMapper {

    /**
     * Maps AccountDTO to Account entity.
     *
     * @param accountDTO The AccountDTO to map.
     * @return Account entity containing the name, balance, open date, and update date from the AccountDTO.
     */
    public Account toEntity(AccountDTO accountDTO) {
        return Account.builder()
                .name(accountDTO.getName())
                .balance(accountDTO.getBalance())
                .open(accountDTO.isOpen())
                .updateAt(accountDTO.getUpdateAt())
                .build();
    }

    /**
     * Maps Account entity to AccountDTO.
     *
     * @param account The Account entity to map.
     * @return AccountDTO containing the name, open date, balance, and update date from the Account entity.
     */
    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .open(account.isOpen())
                .balance(account.getBalance())
                .updateAt(account.getUpdateAt())
                .build();
    }

    /**
     * Maps a list of Account entities to a list of AccountDTOs.
     *
     * @param accounts The list of Account entities to map.
     * @return List of AccountDTOs containing the details of each account in the list.
     */
    public List<AccountDTO> toDTOList(List<Account> accounts) {
        return accounts.stream().map(this::toDTO).toList();
    }
}
