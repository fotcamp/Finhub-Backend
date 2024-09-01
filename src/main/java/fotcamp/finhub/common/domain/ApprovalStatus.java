package fotcamp.finhub.common.domain;

public enum ApprovalStatus {
    PENDING("Pending"),  // 대기 중
    APPROVED("Approved"), // 승인됨
    REJECTED("Rejected"); // 반려됨

    private final String displayName;

    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ApprovalStatus fromDisplayName(String displayName) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid display name: " + displayName);
    }
}
