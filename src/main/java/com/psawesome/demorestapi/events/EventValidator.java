package com.psawesome.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()
                && eventDto.getMaxPrice() != 0) {
            //global error에 담는다
            errors.reject("wrongPrices", "Values for prices are wrong");
        }

        @NotNull LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
                || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
                || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            //field error에 담는다.
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime wrong value");
        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}
