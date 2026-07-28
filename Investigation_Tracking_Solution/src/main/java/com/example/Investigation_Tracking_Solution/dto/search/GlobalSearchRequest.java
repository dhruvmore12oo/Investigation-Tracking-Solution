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
public class GlobalSearchRequest {
    private String keyword;
    private String module;
    private Integer page;
    private Integer size;
}
