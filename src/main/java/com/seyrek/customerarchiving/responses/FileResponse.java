package com.seyrek.customerarchiving.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileResponse {

    Long id;

    String code;

    String name;

    String path;

    String customerName;

    String createdBy;

    Date createdDate;

    String updatedBy;

    Date updatedDate;

}
