package com.app.bankportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_records")
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;

    private String response;
    private int statusCode;
    private LocalDateTime createdAt;

    public String getIdempotencyKey() { 
        return idempotencyKey; 
    }
    public void setIdempotencyKey(String idempotencyKey) { 
        this.idempotencyKey = idempotencyKey; 
    }
    public String getResponse() {
         return response; 
    }
    public void setResponse(String response) { 
        this.response = response; 
    }
    public int getStatusCode() { 
        return statusCode; 
    }
    public void setStatusCode(int statusCode) { 
        this.statusCode = statusCode; 
    }
    public LocalDateTime getCreatedAt() {
         return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt;
    }
}