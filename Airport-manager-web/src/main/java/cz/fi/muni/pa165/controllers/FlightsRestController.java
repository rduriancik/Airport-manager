package cz.fi.muni.pa165.controllers;

import cz.fi.muni.pa165.dto.FlightCreateDTO;
import cz.fi.muni.pa165.dto.FlightDTO;
import cz.fi.muni.pa165.dto.FlightUpdateDTO;
import cz.fi.muni.pa165.exceptions.FlightDataAccessException;
import cz.fi.muni.pa165.exceptions.InvalidRequestException;
import cz.fi.muni.pa165.exceptions.ResourceNotFoundException;
import cz.fi.muni.pa165.exceptions.ServerException;
import cz.fi.muni.pa165.facade.FlightFacade;
import cz.fi.muni.pa165.hateoas.FlightResource;
import cz.fi.muni.pa165.hateoas.FlightResourceAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Robert Duriancik
 */
@RestController
@RequestMapping("/flights")
public class FlightsRestController {

    private FlightFacade flightFacade;
    private FlightResourceAssembler flightResourceAssembler;
    private final static Logger logger = LoggerFactory.getLogger(FlightsRestController.class);


    public FlightsRestController(
            @Autowired FlightFacade flightFacade,
            @Autowired FlightResourceAssembler flightResourceAssembler
    ) {
        this.flightFacade = flightFacade;
        this.flightResourceAssembler = flightResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public final HttpEntity<Resources<FlightResource>> getFlights() {
        List<FlightResource> resourcesCollection = flightResourceAssembler.toResources(flightFacade.getAllFlights());
        Resources<FlightResource> flightResources = new Resources<>(
                resourcesCollection,
                linkTo(FlightsRestController.class).withSelfRel(),
                linkTo(FlightsRestController.class).slash("/create").withRel("create"));
        return new ResponseEntity<>(flightResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public final HttpEntity<FlightResource> getFlight(@PathVariable("id") long id) throws Exception {
        FlightDTO flightDTO = flightFacade.getFlight(id);
        if (flightDTO == null) throw new ResourceNotFoundException("Flight " + id + " not found.");
        FlightResource flightResource = flightResourceAssembler.toResource(flightDTO);
        return new ResponseEntity<>(flightResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public final void deleteFlight(@PathVariable("id") long id) throws Exception {
        try {
            flightFacade.deleteFlight(id);
        } catch (Throwable e) {
            Throwable rootCause = e;
            while ((e = e.getCause()) != null) {
                rootCause = e;
            }

            if (rootCause instanceof IllegalArgumentException) {
                throw new ResourceNotFoundException("Flight " + id + " not found");
            } else {
                throw new ServerException(rootCause);
            }
        }
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public final void updateFlight(@PathVariable("id") long id,
                                   @RequestBody @Valid FlightUpdateDTO flightUpdateDTO,
                                   BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Failed validation");
        }
        if (id != flightUpdateDTO.getId()) {
            throw new InvalidRequestException("Objects differ in ID");
        }

        try {
            flightFacade.updateFlight(flightUpdateDTO);
        } catch (FlightDataAccessException e) {
            throw new ServerException("Server Exception", e);
        }
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public final HttpEntity<Resources<FlightResource>> getCurrentFlights() {
        List<FlightResource> resourcesCollection = flightResourceAssembler.toResources(
                flightFacade.getCurrentFlights(LocalDateTime.now()));

        Resources<FlightResource> flightResources = new Resources<>(resourcesCollection,
                linkTo(FlightsRestController.class).withSelfRel(),
                linkTo(FlightsRestController.class).slash("/create").withRel("create"));

        return new ResponseEntity<>(flightResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public final HttpEntity<FlightResource> createFlight(@RequestBody @Valid FlightCreateDTO flightCreateDTO,
                                                         BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Failed validation");
        }

        Long id = flightFacade.createFlight(flightCreateDTO);
        FlightResource flightResource = flightResourceAssembler.toResource(flightFacade.getFlight(id));
        return new ResponseEntity<>(flightResource, HttpStatus.OK);
    }
}
