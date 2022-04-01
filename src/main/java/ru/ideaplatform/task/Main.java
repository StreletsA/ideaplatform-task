package ru.ideaplatform.task;

import ru.ideaplatform.task.services.TicketService;

import java.io.IOException;


public class Main {

    static TicketService ticketService;

    public static void main(String[] args) throws IOException {

        if (args.length > 0)
            ticketService = new TicketService(args[0]);
        else
            ticketService = new TicketService();

        StringBuilder result = new StringBuilder();

        System.out.println("#######################################");
        System.out.println();
        System.out.println("*****************TASK1*****************");
        System.out.println();
        System.out.println(ticketService.getAverageTime());
        System.out.println("*****************TASK2*****************");
        System.out.println();
        System.out.println(ticketService.getPercentile(90));
        System.out.println();
        System.out.println("#######################################");

    }

}
