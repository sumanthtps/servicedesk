package com.servicedesk.common.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    public static Pageable sanitizePageable(Pageable pageable) {
        int page = Math.max(DEFAULT_PAGE_NUMBER, pageable.getPageNumber());
        int size = Math.min(MAX_PAGE_SIZE, pageable.getPageSize());

        return PageRequest.of(page, size, pageable.getSort());
    }
}
