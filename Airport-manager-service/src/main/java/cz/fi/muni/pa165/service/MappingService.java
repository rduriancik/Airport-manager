package cz.fi.muni.pa165.service;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


/**
 * @author Karel Jiranek
 */
@Service
public interface MappingService {
    /**
     * Map objects of any type to given class.
     *
     * @param objects Objects to be mapped.
     * @param mapToClass Class to be mapped to.
     * @param <T> Type of class.
     * @return List of mapped objects.
     */
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

    /**
     * Map given object to given class.
     *
     * @param object Object to be mapped.
     * @param mapToClass Class to be mapped to.
     * @param <T> Type of class.
     * @return List of mapped objects.
     */
    <T> T mapTo(Object object, Class<T> mapToClass);

    /**
     * Get mapper.
     *
     * @return Mapper
     */
    Mapper getMapper();
}
