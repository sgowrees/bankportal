package com.app.bankportal.service;

import com.app.bankportal.model.IdempotencyRecord;
import com.app.bankportal.repository.IdempotencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    public IdempotencyService(IdempotencyRepository idempotencyRepository) {
        this.idempotencyRepository = idempotencyRepository;
    }

    public Optional<IdempotencyRecord> findByKey(String key) {
        return idempotencyRepository.findById(key);
    }

    public IdempotencyRecord save(String key, String response, int statusCode) {
        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(key);
        record.setResponse(response);
        record.setStatusCode(statusCode);
        record.setCreatedAt(LocalDateTime.now());
        return idempotencyRepository.save(record);
    }
}