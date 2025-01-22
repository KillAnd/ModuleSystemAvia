package tests;

import model.Flight;
import model.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FilterService;
import service.impl.FilterServiceImpl;
import service.impl.FlightBuilderImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FilterServiceTest {

    private FilterServiceImpl filterService;
    private FlightBuilderImpl flightBuilder;
    private LocalDateTime nowTime;

    @BeforeEach
    void setUp() {
        filterService = new FilterServiceImpl();
        flightBuilder = new FlightBuilderImpl();
        nowTime = LocalDateTime.now(); // Текущее время для тестов
    }

    @Test
    void testFilterDepartureBeforeNow() {
        // Создаем тестовые данные
        List<Flight> flights = flightBuilder.createFlights(nowTime);

        // Применяем фильтр
        List<Flight> result = filterService.filterDepartureBeforeNow(flights, nowTime);

        // Проверяем, что в результате нет перелетов с вылетом до текущего времени
        assertTrue(result.stream()
                .noneMatch(flight -> flight.getSegments().get(0).getDepartureDate().isBefore(nowTime)));
    }

    @Test
    void testFilterArrivalBeforeDeparture() {
        // Создаем тестовые данные
        List<Flight> flights = flightBuilder.createFlights(nowTime);

        // Применяем фильтр
        List<Flight> result = filterService.filterArrivalBeforeDeparture(flights);

        // Проверяем, что в результате нет перелетов с сегментами, где прилет раньше вылета
        assertTrue(result.stream()
                .noneMatch(flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()))));
    }

    @Test
    void testFilterExcessiveGroundTime() {
        // Создаем тестовые данные
        List<Flight> flights = flightBuilder.createFlights(nowTime);

        // Применяем фильтр
        List<Flight> result = filterService.filterExcessiveGroundTime(flights);

        // Проверяем, что в результате нет перелетов с общим временем на земле более 2 часов
        assertTrue(result.stream()
                .noneMatch(flight -> {
                    List<Segment> segments = flight.getSegments();
                    Duration totalGroundTime = Duration.ZERO;

                    for (int i = 0; i < segments.size() - 1; i++) {
                        LocalDateTime currentArrival = segments.get(i).getArrivalDate();
                        LocalDateTime nextDeparture = segments.get(i + 1).getDepartureDate();
                        Duration groundTime = Duration.between(currentArrival, nextDeparture);
                        totalGroundTime = totalGroundTime.plus(groundTime);
                    }

                    return totalGroundTime.compareTo(Duration.ofHours(2)) > 0;
                }));
    }
}
