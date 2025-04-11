package com.SpringCodes.journalApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringCodes.journalApplication.models.JournalEntry;

public interface JournalRepository extends JpaRepository<JournalEntry,Long>{
    
}
