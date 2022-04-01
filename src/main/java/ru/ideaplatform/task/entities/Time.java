package ru.ideaplatform.task.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Time {

    private byte departureHours;
    private byte arrivalHours;
    private byte departureMinutes;
    private byte arrivalMinutes;

}
