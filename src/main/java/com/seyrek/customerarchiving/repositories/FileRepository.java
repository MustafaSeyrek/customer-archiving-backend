package com.seyrek.customerarchiving.repositories;

import com.seyrek.customerarchiving.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FileRepository  extends JpaRepository<File, Long> {
    @Transactional
    void deleteByCode(String code);

    File findByCode(String code);
}
