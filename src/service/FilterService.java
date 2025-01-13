package service;

import model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FilterService {

    List<Flight> filterDepartureBeforeNow(List<Flight> flights, LocalDateTime nowTime);

    List<Flight> filterArrivalBeforeDeparture(List<Flight> flights);

    List<Flight> filterExcessiveGroundTime(List<Flight> flights);

}
