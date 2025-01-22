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

        // Фильтр 1: Исключение вылетов
        List<Flight> departureBeforeNow = filter.filterDepartureBeforeNow(flights, nowTime);
        System.out.println("Список с исключеием вылетов до текущего момента времени:");
        departureBeforeNow.forEach(System.out::println);

        // Фильтр 2: Исключение сегментов с датой прилёта раньше даты вылета
        List<Flight> arrivalBeforeDeparture = filter.filterArrivalBeforeDeparture(flights);
        System.out.println("\nСписок с исключением сегментов с датой прилёта раньше даты вылета");
        arrivalBeforeDeparture.forEach(System.out::println);

        // Фильтр 3: Исключение перелетов с общим временем на земле более двух часов
        List<Flight> excessiveGroundTime = filter.filterExcessiveGroundTime(flights);
        System.out.println("\nСписок с исключением перелетов с общим временем на земле более двух часов");
        excessiveGroundTime.forEach(System.out::println);

    }
}
