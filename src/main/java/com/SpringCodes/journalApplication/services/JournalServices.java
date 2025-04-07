package com.SpringCodes.journalApplication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.SpringCodes.journalApplication.models.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.SpringCodes.journalApplication.models.JournalEntry;
import com.SpringCodes.journalApplication.repository.JournalRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JournalServices {

    private final JournalRepository journalRepository;

    private final AppUserService appUserService;

    public JournalServices(JournalRepository journalRepository,AppUserService appUserService){
        this.journalRepository = journalRepository;
        this.appUserService = appUserService;
    }

    @Transactional
    public void addJournal(JournalEntry journalEntry){
        journalRepository.save(journalEntry);
    }

    @Transactional
    public void addJournal(String username, JournalEntry journal) {
        try {
            AppUser user = appUserService.findUserByName(username);
            journal.setDate(LocalDateTime.now());
            journal.setAppUser(user);
            journalRepository.save(journal);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Transaction failed: "+e.getMessage(),e);
        }
    }

    public List<JournalEntry> getAllEntry(){
        return journalRepository.findAll();
    }

    public Optional<JournalEntry> findJournalById(Long id){
        JournalEntry journal = journalRepository.findById(id).orElse(null);
        if(journal==null){
            return Optional.empty();
        }
        return Optional.of(journal);
    }

    public void deleteJournalById(Long x_journal_id,String username){
        AppUser appUser = appUserService.findUserByName(username);
        appUser.getJournalEntryList().removeIf(entry->entry.getId().equals(x_journal_id));
        journalRepository.deleteById(x_journal_id);
    }

    @Transactional
    public ResponseEntity<JournalEntry> updateJournal(Long x_journal_id, JournalEntry journalEntry) {
        try {
            Optional<JournalEntry> journalOptional = journalRepository.findById(x_journal_id);
            if (journalOptional.isPresent()) {
                JournalEntry journal = journalOptional.get();
                journal.setTitle(journalEntry.getTitle());
                journal.setContent(journalEntry.getContent());
                journalRepository.save(journal);
                return new ResponseEntity<>(journal, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Transaction failed: "+e.getMessage(),e);
        }
    }
}

