package com.turf.user.entity.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    SUPER_ADMIN,  // Highest-level admin, manages everything
    TURF_ADMIN,   // Manages turf-related operations
    PLAYER,       // A user who plays on the turf
    COACH,        // A user who trains players
    MANAGER,      // Handles team or event management
    STAFF,        // Ground staff, maintenance, or support personnel
    EVENT_ORGANIZER // Manages tournaments or leagues
}
