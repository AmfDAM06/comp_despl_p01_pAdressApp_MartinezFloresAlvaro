package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.PersonListWrapper;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.PersonPOJO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Jackson person repository.
 */
public class JacksonPersonRepository implements PersonRepository {
        private final ObjectMapper mapper;

    /**
     * Instantiates a new Jackson person repository.
     */
    public JacksonPersonRepository() {
            // Jackson: clase principal para leer/escribir JSON
            this.mapper = new ObjectMapper();
            // Soporte para java.time (LocalDate, LocalDateTime, etc.)
            mapper.registerModule(new JavaTimeModule());
            // Si no lo desactivas, LocalDate puede guardarse como número (timestamp)
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // JSON “bonito” (con saltos de línea e indentación)
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        @Override
        public List<Person> load(File file) throws IOException {
            if (file == null || !file.exists()) {
                return List.of();
            }
            PersonListWrapper wrapper = mapper.readValue(file, PersonListWrapper.class);
            List<Person> out = new ArrayList<>();
            if (wrapper != null && wrapper.persons != null) {
                for (PersonPOJO dto : wrapper.persons) {
                    out.add(Person.fromPOJO(dto)); // tu conversión ya existe :contentReference[oaicite:6]{index=6}
                }
            }
            return out;
        }

        @Override
        public void save(File file, List<Person> persons) throws IOException {
            if (file == null) return;
            if (file.getParentFile() != null) {
                Files.createDirectories(file.toPath().getParent());
            }
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.persons.clear();
            for (Person p : persons) {
                wrapper.persons.add(p.toPOJO());
            }
            mapper.writeValue(file, wrapper);
        }
    }
