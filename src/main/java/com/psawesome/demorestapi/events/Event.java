package com.psawesome.demorestapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Event {

    private Integer id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    private boolean offline;
    private boolean fre;
    private final EventStatus eventStatus = EventStatus.DRAFT;

}
