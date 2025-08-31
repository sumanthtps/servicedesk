package com.servicedesk.common.web;

import com.servicedesk.common.error.InvalidSortFieldException;
import org.springframework.data.domain.Sort;

import java.util.Set;

public class SortUtils {
    public static void validateSortFields(Sort sort, Set<String> allowedFields) {
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            if (!allowedFields.contains(property)) {
                throw new InvalidSortFieldException(property, allowedFields);
            }
        }
    }
}
