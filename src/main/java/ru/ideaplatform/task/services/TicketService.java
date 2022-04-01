package ru.ideaplatform.task.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ideaplatform.task.entities.Ticket;
import ru.ideaplatform.task.entities.TicketsArray;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;




public class TicketService {

    private String fileName = "tickets.json";
    private boolean READ_FROM_ABS_PATH = false;
    private CalculationService calculationService;
    private ObjectMapper objectMapper;

    public TicketService(){
        calculationService = new CalculationService();
        objectMapper = new ObjectMapper();
    }

    public TicketService(String absFilePath){
        this();
        fileName = absFilePath;
        READ_FROM_ABS_PATH = true;
    }

    public List<Ticket> getTickets() throws IOException {

        TicketsArray ticketsArray = objectMapper.readValue(getJson(), TicketsArray.class);

        return ticketsArray.getTickets();

    }

    public String getPercentile(double percentile) throws IOException {

        Ticket percentileTicket = calculationService.getPercentile(getTickets(), percentile);
        double flightTime = calculationService.calculateFlightTime(percentileTicket);

        StringBuilder result = new StringBuilder();

        result.append("90-th percentile calculation result: \n \n")
              .append("Flight time: ")
              .append(flightTime)
              .append(" minutes")
              .append(" -> ")
              .append(calculationService.convertTime(flightTime))
              .append("\n \n")
              .append("TICKET: ")
              .append(percentileTicket.toString());

        return result.toString();
    }

    public String getAverageTime() throws IOException {

        double averageTime = calculateAverageTime();

        StringBuilder result = new StringBuilder();

        result.append("Average time: ")
                .append(averageTime)
                .append(" minutes")
                .append(" -> ")
                .append(calculationService.convertTime(averageTime))
                .append('\n');

        return result.toString();

    }

    private double calculateAverageTime() throws IOException {

        return calculationService.calculateAverageTime(getTickets(), "VVO", "TLV");

    }

    private String getJson() throws FileNotFoundException {

        if (READ_FROM_ABS_PATH)
            return getJson(fileName);

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        }

        StringBuilder json = new StringBuilder();

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json.toString().replace("\uFEFF", "");
    }

    private String getJson(String fileName) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(new File(fileName));

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        }

        StringBuilder json = new StringBuilder();

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json.toString().replace("\uFEFF", "");
    }

}
