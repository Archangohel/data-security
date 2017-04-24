package com.dev.target.service;

import com.dev.security.core.annotations.SecureAccess;
import com.dev.target.entity.Person;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther Archan Gohel
 */
@Component
@SecureAccess
public class PersonService {

    private static final int NUMBER_OF_ELEMENTS = 20;

    public List<Person> getPersonsList() {
        return getPersonsForTest();
    }

    public Map<Person, Object> getPersonKeyMap() {
        return getPersonKeyMapForTest();
    }

    //@SecureAccess
    public Map<Object, Person> getPersonValueMap() {
        return getPersonValueMapForTest();
    }

    private List<Person> getPersonsForTest() {
        List<Person> persons = new ArrayList<>();
        for (int i = NUMBER_OF_ELEMENTS; i > 0; i--) {
            persons.add(new Person(i, "Person" + i));
        }
        return persons;
    }

    private Map<Person, Object> getPersonKeyMapForTest() {
        Map<Person, Object> persons = new HashMap<Person, Object>();
        Object obj = new Object();
        for (int i = NUMBER_OF_ELEMENTS; i > 0; i--) {
            persons.put(new Person(i, "Person" + i), obj);
        }
        return persons;
    }

    private Map<Object, Person> getPersonValueMapForTest() {
        Map<Object, Person> persons = new HashMap<Object, Person>();
        for (int i = NUMBER_OF_ELEMENTS; i > 0; i--) {
            persons.put(new Object(), new Person(i, "Person" + i));
        }
        return persons;
    }

}
