package com.SpringCodes.journalApplication.controllers;

import java.util.List;
import java.util.Optional;

import com.SpringCodes.journalApplication.models.AppUser;
import com.SpringCodes.journalApplication.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringCodes.journalApplication.models.JournalEntry;

import com.SpringCodes.journalApplication.services.JournalServices;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalServices journalServices;

    private final AppUserService userService;

    public JournalController(JournalServices journalServices,AppUserService userService){
        this.journalServices = journalServices;
        this.userService = userService;
    }

    @GetMapping("/check")
    public String getCheck() {
        return "Working Fine";
    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> addEntry(@PathVariable String username,@RequestBody JournalEntry journal) {
      try {
          journalServices.addJournal(username,journal);
          return new ResponseEntity<>(journal,HttpStatus.CREATED);
      }catch (Exception e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    @GetMapping("/journals/lists/username/{username}")
    public ResponseEntity<List<JournalEntry>> getAllJournalsOfUser(@PathVariable String username) {
        AppUser appUser = userService.findUserByName(username);
        List<JournalEntry> journalEntryList = appUser.getJournalEntryList();
        if(journalEntryList!=null && !journalEntryList.isEmpty()){
            return new ResponseEntity<>(journalEntryList,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/{x_journal_id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable Long x_journal_id) {
        Optional<JournalEntry> journal = journalServices.findJournalById(x_journal_id);
        return journal.map(journalEntry -> new ResponseEntity<>(journalEntry, HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{x_journal_id}/{username}")
    public ResponseEntity<?> deleteById(@PathVariable Long x_journal_id,@PathVariable String username) {
        try {
            journalServices.findJournalById(x_journal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal Entry Not Found"));
            journalServices.deleteJournalById(x_journal_id,username);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{username}/{x_journal_id}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable Long x_journal_id,@PathVariable String username, @RequestBody JournalEntry newEntry) {
        Optional<JournalEntry> optionalOld = journalServices.findJournalById(x_journal_id);
        if (optionalOld.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        JournalEntry old = optionalOld.get();
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            old.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            old.setContent(newEntry.getContent());
        }
        journalServices.addJournal(old);
        return ResponseEntity.ok(old);
    }
}

