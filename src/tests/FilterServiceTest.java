package tests;

import model.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.FilterService;
import service.FlightBuilder;
import service.impl.FilterServiceImpl;
import service.impl.FlightBuilderImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterServiceTest {

    @Test
    void testFilterDepartureBeforeNow() {
        FilterService filter = new FilterServiceImpl();
        FlightBuilder flightBuilder = new FlightBuilderImpl();
        LocalDateTime nowTime = LocalDateTime.now();

        // Создаем тестовые данные
        List<Flight> flights = Arrays.asList(
                flightBuilder.createFlight(nowTime.minusHours(1), nowTime.plusHours(1)), // Вылет в прошлом
                flightBuilder.createFlight(nowTime.plusHours(1), nowTime.plusHours(3))  // Вылет в будущем
        );

        // Применяем фильтр
        List<Flight> result = filter.filterDepartureBeforeNow(flights, nowTime);

        // Проверяем, что остался только один перелет с вылетом в прошлом
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSegments().get(0).getDepartureDate().isBefore(nowTime));
    }

    @Test
    void testFilterArrivalBeforeDeparture() {
        FilterService filter = new FilterServiceImpl();
        FlightBuilder flightBuilder = new FlightBuilderImpl();

        // Создаем тестовые данные
        List<Flight> flights = Arrays.asList(
                flightBuilder.createFlight(LocalDateTime.now(), LocalDateTime.now().plusHours(2)), // Нормальный сегмент
                flightBuilder.createFlight(LocalDateTime.now(), LocalDateTime.now().minusHours(1)) // Прилет раньше вылета
        );

        // Применяем фильтр
        List<Flight> result = filter.filterArrivalBeforeDeparture(flights);

        // Проверяем, что остался только один перелет с прилетом раньше вылета
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSegments().get(0).getArrivalDate().isBefore(result.get(0).getSegments().get(0).getDepartureDate()));
    }

    @Test
    void testFilterExcessiveGroundTime() {
        FilterService filter = new FilterServiceImpl();
        FlightBuilder flightBuilder = new FlightBuilderImpl();

        // Создаем тестовые данные
        List<Flight> flights = Arrays.asList(
                flightBuilder.createFlight(LocalDateTime.now(), LocalDateTime.now().plusHours(2)), // Нет времени на земле
                flightBuilder.createFlight(LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                        LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6)) // 3 часа на земле
        );

        // Применяем фильтр
        List<Flight> result = filter.filterExcessiveGroundTime(flights);

        // Проверяем, что остался только один перелет с временем на земле более 2 часов
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSegments().size() > 1); // Убедимся, что это многосегментный перелет
    }

}
