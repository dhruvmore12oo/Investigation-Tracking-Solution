package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.dto.search.GlobalSearchResponse;
import com.example.Investigation_Tracking_Solution.model.User;

public interface GlobalSearchService {
    GlobalSearchResponse globalSearch(String keyword, String module, int page, int size, User currentUser);
}
