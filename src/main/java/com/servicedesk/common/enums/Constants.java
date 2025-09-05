package com.servicedesk.common.enums;

import java.util.Map;
import java.util.Set;

public class Constants {
    public static final Map<Status, Set<Status>> ALLOWED_TRANSITIONS = Map.of(
            Status.OPEN, Set.of(Status.IN_PROGRESS, Status.RESOLVED),
            Status.IN_PROGRESS, Set.of(Status.OPEN, Status.RESOLVED),
            Status.RESOLVED, Set.of(Status.IN_PROGRESS, Status.CLOSED),
            Status.CLOSED, Set.of()
    );
}
