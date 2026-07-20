package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalRequest;
import com.example.Investigation_Tracking_Solution.dto.criminal.CriminalResponse;
import com.example.Investigation_Tracking_Solution.service.CriminalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/criminals")
@RequiredArgsConstructor
public class CriminalController {

    private final CriminalService criminalService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CriminalResponse> createCriminal(@Valid @RequestBody CriminalRequest request) {
        CriminalResponse response = criminalService.createCriminal(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<CriminalResponse> getCriminalById(@PathVariable Long id) {
        CriminalResponse response = criminalService.getCriminalById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<CriminalResponse>> getAllCriminals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(criminalService.getAllCriminals(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVESTIGATOR')")
    public ResponseEntity<CriminalResponse> updateCriminal(
            @PathVariable Long id,
            @Valid @RequestBody CriminalRequest request) {
        return ResponseEntity.ok(criminalService.updateCriminal(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCriminal(@PathVariable Long id) {
        criminalService.deleteCriminal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<List<CriminalResponse>> searchCriminalsByName(@RequestParam String name) {
        return ResponseEntity.ok(criminalService.searchByName(name));
    }

    @GetMapping("/aadhaar/{aadhaar}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<CriminalResponse> getCriminalByAadhaar(@PathVariable String aadhaar) {
        return ResponseEntity.ok(criminalService.getByAadhaar(aadhaar));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<CriminalResponse>> getCriminalsByStatus(
            @PathVariable com.example.Investigation_Tracking_Solution.model.CriminalStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(criminalService.getByStatus(status, page, size));
    }

    @GetMapping("/risk/{riskLevel}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<CriminalResponse>> getCriminalsByRiskLevel(
            @PathVariable com.example.Investigation_Tracking_Solution.model.RiskLevel riskLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(criminalService.getByRiskLevel(riskLevel, page, size));
    }
}
