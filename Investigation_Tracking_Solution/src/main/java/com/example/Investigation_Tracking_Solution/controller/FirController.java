package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.fir.FirRequest;
import com.example.Investigation_Tracking_Solution.dto.fir.FirResponse;
import com.example.Investigation_Tracking_Solution.model.FirStatus;
import com.example.Investigation_Tracking_Solution.service.FirService;
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
@RequestMapping("/firs")
@RequiredArgsConstructor
public class FirController {

    private final FirService firService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<FirResponse> createFir(@Valid @RequestBody FirRequest request) {
        return new ResponseEntity<>(firService.createFir(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<FirResponse> getFirById(@PathVariable Long id) {
        return ResponseEntity.ok(firService.getFirById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<FirResponse>> getAllFirs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(firService.getAllFirs(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<FirResponse> updateFir(
            @PathVariable Long id,
            @Valid @RequestBody FirRequest request) {
        return ResponseEntity.ok(firService.updateFir(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFir(@PathVariable Long id) {
        firService.deleteFir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/number/{firNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<FirResponse> getByFirNumber(@PathVariable String firNumber) {
        return ResponseEntity.ok(firService.getByFirNumber(firNumber));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<FirResponse>> getByStatus(
            @PathVariable FirStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(firService.getByStatus(status, page, size));
    }

    @GetMapping("/officer/{officerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<FirResponse>> getByOfficerId(
            @PathVariable Long officerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(firService.getByOfficerId(officerId, page, size));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<FirResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(firService.getByDateRange(startDate, endDate, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<Page<FirResponse>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(firService.searchByKeyword(keyword, page, size));
    }
}
