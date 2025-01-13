package service;


import model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightBuilder {
    List<Flight> createFlights(LocalDateTime currentTime);

    Flight createFlight(final LocalDateTime... dates);
}
