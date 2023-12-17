package com.seyrek.customerarchiving.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data

public class CustomerResponse {
    Long id;

    String fullName;

    String createdBy;

    Date createdAt;

    String updatedBy;

    Date updatedAt;
    public CustomerResponse(Long id, String fullName, String createdBy, Date createdAt, String updatedBy, Date updatedAt){
        this.id = id;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        if(updatedBy == null) this.updatedBy="";else this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
