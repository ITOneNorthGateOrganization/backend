package com.northgatevologda.smartbudget.infrastructure.jpa.transactiontype;

import com.northgatevologda.smartbudget.domain.model.TransactionType;
import com.northgatevologda.smartbudget.domain.ports.out.TransactionTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JpaTransactionTypeRepositoryAdapter implements TransactionTypeRepositoryPort {

    private final JpaTransactionTypeRepository jpaTransactionTypeRepository;

    @Override
    public Optional<TransactionType> findById(Long id) {
        return jpaTransactionTypeRepository.findById(id);
    }
}
