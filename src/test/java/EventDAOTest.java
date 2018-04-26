/*
 * Classe de proves per tal de provar el funcionament de la classe EventDAO sobre la BD MySQL d'EventFest
 */

/**
 *
 * @author Albert
 */

/*import domain.Event;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import domain.repository.DBConnection;
import domain.repository.EventDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//Tests de cadascun dels métodes usats pel servei i que consulten o modifiquen la base de dades
public class EventDAOTest {

    private DBConnection dBConnection;
    private String connectionProperties = "db_test.properties";
    EventDAO eventDAO;

    @Before
    public void setUp() {
        //Establim connexió amb la BD
        dBConnection = new DBConnection(connectionProperties);
        eventDAO = new EventDAO(dBConnection);
    }

    @After
    public void tearDown() throws IOException, SQLException {
        //Tanquem la connexió amb la BD
        eventDAO.getConnection().close();
    }

    @Test
    //test que compta el nombre d'events dins la base de dades
    public void countUsers() throws Exception {
        int events = eventDAO.countEvents();
        Assert.assertEquals("Hauriem de tenir 2 events a la base de dades", 2, events);
    }
    
    @Test
    //test que obté tots els events dins la base de dades
    public void getAllEvents() throws SQLException {
        List<Event> events = eventDAO.getAllEvents();
        //comprovem la mida de la llista amb el nombre a BD
        Assert.assertEquals("Hauriem de tenir 2 usuaris a la base de dades", 2, events.size());
    }


    @Test
    public void addEvent() throws Exception {
        String event_title = "Nou Event";
        String event_startdate = "2018-04-02";
        String event_finishdate = "2018-04-02";
        String event_type = "Festa";
        String event_description = "Una prova";
        String event_address = "un carrer";
        String event_city = "Barcelona";
        int users_id = 2;
        Event event = new Event("3", event_title, event_startdate, event_finishdate, event_type, event_description, event_address, event_city, null, users_id);
        Event createdEvent = eventDAO.addEvent(event);
        //Verifiquem s'ha creat l'event
        Assert.assertNotNull(createdEvent);
        //Verifiquem que el titol de l'event creat i el de la BD coincideix
        Assert.assertEquals(event_title, createdEvent.getTitle());
        //Verifiquem que el seu id no és 0
        Assert.assertNotEquals(0, createdEvent.getId());
        
        //fer que l'elimini per a que la bateria de tests continui
        eventDAO.deleteEvent(String.valueOf(createdEvent.getId()));
    }

    @Test
    public void updateEvent() throws Exception {
        String event_title = "Nou Event";
        String event_startdate = "2018-04-02";
        String event_finishdate = "2018-04-02";
        String event_type = "Festa";
        String event_description = "Una prova";
        String event_address = "un carrer";
        String event_city = "Barcelona";
        int users_id = 2;
        Event event = new Event("3", event_title, event_startdate, event_finishdate, event_type, event_description, event_address, event_city, null, users_id);
        Event createdEvent = eventDAO.addEvent(event);
        //Verifiquem s'ha creat l'event
        Assert.assertNotNull(createdEvent);
        //Verifiquem que el titol de l'event creat i el de la BD coincideix
        Assert.assertEquals(event_title, createdEvent.getTitle());
        Event eventModificat = new Event("3", "canviat", event_startdate, event_finishdate, event_type, event_description, event_address, event_city, null, users_id);
        //Fem el canvi del titol
        Event updatedEvent = eventDAO.updateEvent(eventModificat, "3");
        //Verifiquem que el id de l'usuari creat i el de la BD modificat coincideix
        Assert.assertEquals(createdEvent.getId(), updatedEvent.getId());
        //Verifiquem que el nou email de l'usuari creat i el de la BD coincideix
        Assert.assertEquals("canviat", updatedEvent.getTitle());
        
        //fer que l'elimini per a que la bateria de tests continui
        eventDAO.deleteEvent(String.valueOf(createdEvent.getId()));
    }
    
    @Test
    public void deleteEvent() throws Exception {
        String event_title = "Nou Event";
        String event_startdate = "2018-04-02";
        String event_finishdate = "2018-04-02";
        String event_type = "Festa";
        String event_description = "Una prova";
        String event_address = "un carrer";
        String event_city = "Barcelona";
        int users_id = 2;
        Event event = new Event("3", event_title, event_startdate, event_finishdate, event_type, event_description, event_address, event_city, null, users_id);
        Event createdEvent = eventDAO.addEvent(event);
        //Verifiquem s'ha creat l'event
        Assert.assertNotNull(createdEvent);
        //eliminem l'event
        eventDAO.deleteEvent(String.valueOf(createdEvent.getId()));
        //Intentem obtenir de nou l'usuari eliminat
        Event deletedEvent = eventDAO.getEventById(createdEvent.getId());
        //Verifiquem que l'usuari no s'ha trobat
        Assert.assertNull(deletedEvent);
    }


}
*/

