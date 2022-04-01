package ru.ideaplatform.task.services;

import ru.ideaplatform.task.entities.Ticket;
import ru.ideaplatform.task.entities.Time;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

public class CalculationService {

    public double calculateAverageTime(List<Ticket> tickets, String origin, String destination)
    {

        AtomicLong avrTime = new AtomicLong();

        tickets.forEach(ticket -> avrTime.addAndGet(calculateFlightTime(ticket)));

        return avrTime.doubleValue() / tickets.size();
    }

    public Ticket getPercentile(List<Ticket> tickets, double percentile) {

        List<Ticket> sortedList = tickets.stream().sorted((t1, t2) -> {
            long flightTime1 = calculateFlightTime(t1);
            long flightTime2 = calculateFlightTime(t2);

            if (flightTime1 > flightTime2) return 1;
            if (flightTime1 < flightTime2) return -1;
            else return 0;

        }).collect(toList());

        int index = (int) Math.ceil(percentile / 100.0 * sortedList.size());
        return sortedList.get(index - 1);
    }

    public long calculateFlightTime(Ticket ticket)
    {
        long result = 0;

        Time derivedTime = deriveTime(ticket);

        byte departureHours = derivedTime.getDepartureHours();
        byte arrivalHours = derivedTime.getArrivalHours();
        byte departureMinutes = derivedTime.getDepartureMinutes();
        byte arrivalMinutes = derivedTime.getArrivalMinutes();

        if (ticket.getDeparture_date().equals(ticket.getArrival_date())){

            if (arrivalHours < departureHours){

                result = (arrivalHours - departureHours - 1)*60 + (arrivalMinutes + 60 - departureMinutes);

            }
            else {
                result = (arrivalHours - departureHours)*60 + (arrivalMinutes - departureMinutes);
            }
        }
        else{
            // There's no such situation in the file, so I didn't write logic for it
        }

        return result;
    }

    public String convertTime(double minutes)
    {
        int hours = (int) (minutes / 60);
        minutes -= hours * 60;

        return new StringBuilder().append(hours).append(":").append((int)minutes).toString();
    }

    private Time deriveTime(Ticket ticket)
    {
        byte departureHours = 0;
        byte arrivalHours = 0;
        byte departureMinutes = 0;
        byte arrivalMinutes = 0;

        String[] departureTime = ticket.getDeparture_time().split(":");
        String[] arrivalTime = ticket.getArrival_time().split(":");

        departureHours = Byte.parseByte(departureTime[0]);
        departureMinutes = Byte.parseByte(departureTime[1]);

        arrivalHours = Byte.parseByte(arrivalTime[0]);
        arrivalMinutes = Byte.parseByte(arrivalTime[1]);

        return new Time(departureHours, arrivalHours, departureMinutes, arrivalMinutes);
    }



}
