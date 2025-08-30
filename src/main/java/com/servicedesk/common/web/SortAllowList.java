package com.servicedesk.common.web;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortAllowList {
    public static final Set<String> ISSUE_SORT_FIELDS = Stream.of("createdAt", "updatedAt", "priority")
            .collect(Collectors.toSet());

    public static final Set<String> PROJECT_SORT_FIELDS = Stream.of("name", "key", "createdAt", "updatedAt")
            .collect(Collectors.toSet());

    public static final Set<String> USER_SORT_FIELDS = Stream.of("username", "email", "createdAt")
            .collect(Collectors.toSet());
}
