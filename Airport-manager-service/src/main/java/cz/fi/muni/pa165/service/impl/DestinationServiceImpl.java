package cz.fi.muni.pa165.service.impl;

import cz.fi.muni.pa165.dao.DestinationDao;
import cz.fi.muni.pa165.dao.FlightDao;
import cz.fi.muni.pa165.entities.Destination;
import cz.fi.muni.pa165.entities.Flight;
import cz.fi.muni.pa165.exceptions.DestinationDataAccessException;
import cz.fi.muni.pa165.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DestinationService interface
 *
 * @author Ondřej Přikryl
 */
@Service
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationDao destinationDao;

    @Autowired
    private FlightDao flightDao;

    @Override
    public Long createDestination(String country, String city) {
        Destination destination = new Destination();
        destination.setCountry(country);
        destination.setCity(city);

        try {
            destinationDao.addDestination(destination);
            return destination.getId();
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while adding destination: " + destination, e);
        }
    }

    @Override
    public void removeDestination(Destination destination) {
        try {
            destinationDao.removeDestination(destination);
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while removing destination: " + destination, e);
        }
    }

    @Override
    public void updateDestination(Destination destination) {
        try {
            destinationDao.updateDestination(destination);
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while updating destination: " + destination, e);
        }
    }

    @Override
    public Destination getDestinationById(Long id) {
        try {
            return destinationDao.getDestination(id);
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while getting destination by ID.", e);
        }
    }

    @Override
    public List<Destination> getDestinationsByCountry(String country) {
        try {
            return destinationDao.getDestinationsByCountry(country);
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while getting destination by country: " + country, e);
        }
    }

    @Override
    public List<Destination> getDestinationsByCity(String city) {
        try {
            return destinationDao.getDestinationsByCity(city);
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while getting destination by city: " + city, e);
        }
    }

    @Override
    public List<Destination> getAllDestinations() {
        return destinationDao.getAllDestinations();
    }

    @Override
    public List<Flight> getAllIncomingFlights(Destination destination) {
        try {
            List<Flight> flights = flightDao.getAllFlights();
            List<Flight> incoming = new ArrayList<>();
            for(Flight flight : flights) {
                if(flight.getArrivalLocation().equals(destination)) {
                    incoming.add(flight);
                }
            }
            return incoming;
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while getting all incoming flights in destination: " + destination, e);
        }
    }

    @Override
    public List<Flight> getAllOutgoingFlights(Destination destination) {
        try {
            List<Flight> flights = flightDao.getAllFlights();
            List<Flight> outgoing = new ArrayList<>();
            for(Flight flight : flights) {
                if(flight.getDepartureLocation().equals(destination)) {
                    outgoing.add(flight);
                }
            }
            return outgoing;
        } catch (Exception e) {
            throw new DestinationDataAccessException("Exception while getting all outgoing flights in destination: " + destination, e);
        }
    }
}
