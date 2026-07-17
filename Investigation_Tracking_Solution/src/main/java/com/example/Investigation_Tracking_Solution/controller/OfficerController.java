package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.officer.OfficerRequest;
import com.example.Investigation_Tracking_Solution.dto.officer.OfficerResponse;
import com.example.Investigation_Tracking_Solution.service.OfficerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officers")
@RequiredArgsConstructor
public class OfficerController {

    private final OfficerService officerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficerResponse> createOfficer(@Valid @RequestBody OfficerRequest request) {
        OfficerResponse response = officerService.createOfficer(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<OfficerResponse> getOfficer(@PathVariable Long id) {
        OfficerResponse response = officerService.getOfficerById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<Page<OfficerResponse>> getAllOfficers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(officerService.getAllOfficers(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficerResponse> updateOfficer(@PathVariable Long id, @Valid @RequestBody OfficerRequest request) {
        return ResponseEntity.ok(officerService.updateOfficer(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<Void> deleteOfficer(@PathVariable Long id) {
        officerService.deleteOfficer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/badge/{badgeNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public ResponseEntity<OfficerResponse> getOfficerByBadgeNo(@PathVariable String badgeNumber) {
        return ResponseEntity.ok(officerService.getOfficerByBadgeNumber(badgeNumber));
    }


}
