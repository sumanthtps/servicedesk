package com.servicedesk.context;

import java.util.UUID;

public class OrganizationContext {
    private static final ThreadLocal<UUID> currentOrgId = new ThreadLocal<>();

    public static void setOrgId(UUID orgId) {
        currentOrgId.set(orgId);
    }

    public static UUID getOrgId() {
        return currentOrgId.get();
    }

    public static void clear() {
        currentOrgId.remove();
    }
}
