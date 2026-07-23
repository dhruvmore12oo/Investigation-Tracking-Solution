package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.witness.WitnessRequest;
import com.example.Investigation_Tracking_Solution.dto.witness.WitnessResponse;
import com.example.Investigation_Tracking_Solution.model.ReliabilityLevel;
import com.example.Investigation_Tracking_Solution.model.WitnessStatus;
import com.example.Investigation_Tracking_Solution.service.WitnessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/witnesses")
@RequiredArgsConstructor
public class WitnessController {

    private final WitnessService witnessService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<WitnessResponse> createWitness(@Valid @RequestBody WitnessRequest request) {
        return new ResponseEntity<>(witnessService.createWitness(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<WitnessResponse> getWitnessById(@PathVariable Long id) {
        return ResponseEntity.ok(witnessService.getWitnessById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<WitnessResponse>> getAllWitnesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(witnessService.getAllWitnesses(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<WitnessResponse> updateWitness(
            @PathVariable Long id,
            @Valid @RequestBody WitnessRequest request) {
        return ResponseEntity.ok(witnessService.updateWitness(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWitness(@PathVariable Long id) {
        witnessService.deleteWitness(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/number/{witnessNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<WitnessResponse> getByWitnessNumber(@PathVariable String witnessNumber) {
        return ResponseEntity.ok(witnessService.getByWitnessNumber(witnessNumber));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<WitnessResponse>> getByCase(
            @PathVariable Long caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(witnessService.getByCase(caseId, page, size));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<WitnessResponse>> getByStatus(
            @PathVariable WitnessStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(witnessService.getByStatus(status, page, size));
    }

    @GetMapping("/reliability/{level}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<WitnessResponse>> getByReliabilityLevel(
            @PathVariable ReliabilityLevel level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(witnessService.getByReliabilityLevel(level, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<WitnessResponse>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(witnessService.searchByKeyword(keyword, page, size));
    }
}
