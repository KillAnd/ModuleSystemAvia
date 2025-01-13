package service.impl;

import model.Flight;
import model.Segment;
import service.FilterService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FilterServiceImpl implements FilterService {


    // Фильтр 1: Вылет до текущего момента времени
    @Override
    public List<Flight> filterDepartureBeforeNow(List<Flight> flights, LocalDateTime nowTime) {
        return flights.stream()
                .filter(flight -> flight.getSegments().get(0).getDepartureDate().isBefore(nowTime))
                .collect(Collectors.toList());
    }

    // Фильтр 2: Сегменты с датой прилёта раньше даты вылета
    @Override
    public List<Flight> filterArrivalBeforeDeparture(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }

    // Фильтр 3: Перелеты, где общее время на земле превышает два часа
    @Override
    public List<Flight> filterExcessiveGroundTime(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    Duration totalGroundTime = Duration.ZERO;

                    for (int i = 0; i < segments.size() - 1; i++) {
                        LocalDateTime currentArrival = segments.get(i).getArrivalDate();
                        LocalDateTime nextDeparture = segments.get(i + 1).getDepartureDate();
                        Duration groundTime = Duration.between(currentArrival, nextDeparture);
                        totalGroundTime = totalGroundTime.plus(groundTime);
                    }

                    return totalGroundTime.compareTo(Duration.ofHours(2)) > 0;
                })
                .collect(Collectors.toList());
    }
}
