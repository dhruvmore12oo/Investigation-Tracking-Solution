package com.example.Investigation_Tracking_Solution.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSearchItemResponse {
    private String module;
    private Long entityId;
    private String title;
    private String subtitle;
    private String description;
    private String referenceNumber;
}
