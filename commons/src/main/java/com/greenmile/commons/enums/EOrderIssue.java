package com.greenmile.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum EOrderIssue {

    WITHOUT_CAPACITY(1L, "No routes available with capacity"),
    WITHOUT_DISTANCE(2L, "No routes available with distance");

    private Long id;
    private String description;

    public static EOrderIssue findById(final Long id) {
        return Arrays.stream(EOrderIssue.values()).filter(issue -> issue.getId().compareTo(id) == 0).findFirst().orElse(null);
    }

}
