import model.Flight;
import service.FilterService;
import service.FlightBuilder;
import service.impl.FilterServiceImpl;
import service.impl.FlightBuilderImpl;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FilterService filter = new FilterServiceImpl();
        FlightBuilder flightBuilder = new FlightBuilderImpl();
        LocalDateTime nowTime = LocalDateTime.now();
        List<Flight> flights = flightBuilder.createFlights(nowTime);

        // Фильтр 1: Вылет до текущего момента времени
        List<Flight> departureBeforeNow = filter.filterDepartureBeforeNow(flights, nowTime);
        System.out.println("Перелеты с вылетом до текущего момента времени:");
        departureBeforeNow.forEach(System.out::println);

        // Фильтр 2: Сегменты с датой прилёта раньше даты вылета
        List<Flight> arrivalBeforeDeparture = filter.filterArrivalBeforeDeparture(flights);
        System.out.println("\nПерелеты с сегментами, где прилет раньше вылета:");
        arrivalBeforeDeparture.forEach(System.out::println);

        // Фильтр 3: Перелеты с общим временем на земле более двух часов
        List<Flight> excessiveGroundTime = filter.filterExcessiveGroundTime(flights);
        System.out.println("\nПерелеты с общим временем на земле более двух часов:");
        excessiveGroundTime.forEach(System.out::println);

    }
}
