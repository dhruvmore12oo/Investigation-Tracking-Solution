package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.search.GlobalSearchResponse;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.service.GlobalSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class GlobalSearchController {

    private final GlobalSearchService globalSearchService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER', 'INVESTIGATOR')")
    public ResponseEntity<GlobalSearchResponse> search(
            @RequestParam String keyword,
            @RequestParam(required = false) String module,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser) {
        GlobalSearchResponse response = globalSearchService.globalSearch(keyword, module, page, size, currentUser);
        return ResponseEntity.ok(response);
    }
}
