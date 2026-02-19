package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence;

import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The interface Person repository.
 */
public interface PersonRepository {
    /**
     * Load list.
     *
     * @param file the file
     * @return the list
     * @throws IOException the io exception
     */
    List<Person> load(File file) throws IOException;

    /**
     * Save.
     *
     * @param file    the file
     * @param persons the persons
     * @throws IOException the io exception
     */
    void save(File file, List<Person> persons) throws IOException;
}