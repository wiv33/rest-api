package com.psawesome.demorestapi.events;

import com.psawesome.demorestapi.accounts.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
public class Event {

    @Id
    @GeneratedValue
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
    private boolean free;
    @Enumerated(EnumType.STRING) @Builder.Default
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    private Account manager;

    public void update() {
        // Update free
        this.free = (this.basePrice == 0 && this.maxPrice == 0) ? true : false;
        this.offline = (Objects.isNull(this.location) || this.location.isBlank() ? false : true);
    }
}
