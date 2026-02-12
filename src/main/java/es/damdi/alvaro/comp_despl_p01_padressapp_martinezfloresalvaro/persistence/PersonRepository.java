package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence;

import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PersonRepository {
    List<Person> load(File file) throws IOException;
    void save(File file, List<Person> persons) throws IOException;
}