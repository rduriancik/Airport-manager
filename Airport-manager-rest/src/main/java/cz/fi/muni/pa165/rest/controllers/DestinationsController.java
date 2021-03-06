package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.dto.DestinationDTO;
import cz.fi.muni.pa165.dto.NewDestinationDTO;
import cz.fi.muni.pa165.facade.DestinationFacade;
import cz.fi.muni.pa165.rest.Uris;
import cz.fi.muni.pa165.rest.exceptions.ResourceAlreadyExistingException;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/** *
 * @author Karel Jiranek
 */
@RestController
@RequestMapping(Uris.ROOT_URI_DESTINATIONS)
public class DestinationsController {
    @Inject
    private DestinationFacade destinationFacade;


    /**
     * Get destinations
     *
     * @return All destinations
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DestinationDTO> getDestinations() {
        return destinationFacade.getAllDestinations();
    }

    /**
     * Get destination by id.
     * @param id Id of destination.
     * @return Destination with given destination or raise the exception.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final DestinationDTO getDestinationsByID(@PathVariable("id") long id)  {
        DestinationDTO DestinationDTO = destinationFacade.getDestinationById(id);
        if (DestinationDTO != null) {
            return DestinationDTO;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Get all destinations by country.
     * @param country Country of destination.
     * @return Destination with given country.
     */
    @RequestMapping(value = "/all/{country}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DestinationDTO> getDestinationsByCountry(@PathVariable("country") String country)  {
        return destinationFacade.getDestinationsByCountry(country);
    }

    /**
     * Create destination.
     *
     * @param newDestinationDTO Destination to be created.
     * @return Created destination.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final DestinationDTO createDestination(@RequestBody NewDestinationDTO newDestinationDTO)  {
        try {
            Long id = destinationFacade.createDestination(newDestinationDTO.getCountry(), newDestinationDTO.getCity());
            return destinationFacade.getDestinationById(id);
        } catch (Exception ex) {
            throw new ResourceAlreadyExistingException();
        }
    }

    /**
     * Delete destination by id
     * @throws ResourceNotFoundException If resource not found.
     * @param id Id of destination to be deleted.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void removeDestination(@PathVariable("id") long id) {
        try {
            destinationFacade.removeDestination(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

}
