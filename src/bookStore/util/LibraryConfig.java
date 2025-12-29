package bookStore.util;

import bookStore.dependesies.annotation.ConfigProperty;

public class LibraryConfig {@ConfigProperty(propertyName = "book.stale.months", type = Integer.class)
private int staleMonths;

    @ConfigProperty(propertyName = "book.auto.close.requests", type = Boolean.class)
    private boolean autoCloseRequests;

    public int getStaleMonths() {
        return staleMonths;
    }

    public boolean isAutoCloseRequests() {
        return autoCloseRequests;
    }

}
