package com.dev;

import com.dev.config.AppConfig;
import com.dev.security.core.utils.CreateDefaultUserAccessMappings;
import com.dev.target.entity.Person;
import com.dev.target.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @auther Archan Gohel
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
/*@PropertySources({
        @PropertySource(value = "classpath:global.properties", ignoreResourceNotFound = false)})*/
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CreateDefaultUserAccessMappings defaultUserAccessMappingService = context.getBean(CreateDefaultUserAccessMappings.class);

        defaultUserAccessMappingService.createDefaultUserAccessMappings();

        PersonService personService = context.getBean(PersonService.class);
        final long startTime1 = System.currentTimeMillis();
        List<Person> persons = personService.getPersonsList();
        final long endTime1 = System.currentTimeMillis();

        logger.info("{} person objects found. Object(s) [ {} ].\n " +
                "Time taken is {} ms.", persons.size(), persons, endTime1 - startTime1);

        final long startTime2 = System.currentTimeMillis();
        Map<Person, Object> personsKeyMap = personService.getPersonKeyMap();
        final long endTime2 = System.currentTimeMillis();

        logger.info("{} person objects found. Object(s) [ {} ].\n" +
                " Time taken is {} ms.", personsKeyMap.size(), personsKeyMap, endTime2 - startTime2);

        final long startTime3 = System.currentTimeMillis();
        Map<Object, Person> personsValueMap = personService.getPersonValueMap();
        final long endTime3 = System.currentTimeMillis();

        logger.info("{} person objects found. Object(s) [ {} ].\n" +
                " Time taken is {} ms.", personsValueMap.size(), personsValueMap, endTime3 - startTime3);
    }


}
