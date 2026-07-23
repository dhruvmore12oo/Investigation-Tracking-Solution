package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationRequest;
import com.example.Investigation_Tracking_Solution.dto.investigation.InvestigationResponse;
import com.example.Investigation_Tracking_Solution.model.CasePriority;
import com.example.Investigation_Tracking_Solution.model.InvestigationStatus;
import com.example.Investigation_Tracking_Solution.service.InvestigationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/investigations")
@RequiredArgsConstructor
public class InvestigationController {

    private final InvestigationService investigationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<InvestigationResponse> createInvestigation(@Valid @RequestBody InvestigationRequest request) {
        return new ResponseEntity<>(investigationService.createInvestigation(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationResponse> getInvestigationById(@PathVariable Long id) {
        return ResponseEntity.ok(investigationService.getInvestigationById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getAllInvestigations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getAllInvestigations(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationResponse> updateInvestigation(
            @PathVariable Long id,
            @Valid @RequestBody InvestigationRequest request) {
        return ResponseEntity.ok(investigationService.updateInvestigation(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInvestigation(@PathVariable Long id) {
        investigationService.deleteInvestigation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/number/{investigationNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<InvestigationResponse> getByInvestigationNumber(@PathVariable String investigationNumber) {
        return ResponseEntity.ok(investigationService.getByInvestigationNumber(investigationNumber));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getByStatus(
            @PathVariable InvestigationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getByStatus(status, page, size));
    }

    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getByPriority(
            @PathVariable CasePriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getByPriority(priority, page, size));
    }

    @GetMapping("/investigator/{investigatorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getByInvestigator(
            @PathVariable Long investigatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getByInvestigator(investigatorId, page, size));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getByCase(
            @PathVariable Long caseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getByCase(caseId, page, size));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.getByDateRange(startDate, endDate, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<InvestigationResponse>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(investigationService.searchByKeyword(keyword, page, size));
    }
}
