/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.repository;

import domain.Event;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Albert
 */
public class EventDAO {
    private DBConnection dBConnection;
    private Connection connection;
    
    public EventDAO(DBConnection dBConnection) {
        this.dBConnection = dBConnection;
    }
    

    public EventDAO() {
        try {
            dBConnection = (DBConnection) new InitialContext().lookup("java:global/eventfest/DBConnection");
            dBConnection.setConnectionFile("db.properties");
        } catch (NamingException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Get event by Id
     * Funció que retorna les dades de l'event corresponent al Id associat
     * @throws java.lang.Exception
     * @param event_id Int
     * @return type objecte event amb l'ID demanat
     */
    public Event getEventById(int event_id) throws Exception{
        String qry = "select * from events where id = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setInt(1, event_id);
        return findUniqueResult(preparedStatement);
    }
    
    /**
     * Count events
     * Funció que retorna el nombre de registres d'events dins la BD
     * @throws java.lang.Exception
     * @return type int nombre events
     */
    public int countEvents() throws Exception{
        //Por defecto no hay resultados       
        int n=0;
        String qry = "SELECT count(*) FROM events";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        
        try {
            ResultSet rs = preparedStatement.executeQuery(); 
            if(rs.next()) {
                //Si hay resultados obtengo el valor. 
                 n= rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return n;
    }
    
    /**
     * Get All events
     * Funció que retorna tots els events dins la taula events de la BD
     * @throws java.sql.SQLException
     * @return type List ArrayList amb tots els events de la BD
     */
    public List<Event> getAllEvents() throws SQLException {
        String qry = "select * from events";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    /**
     * Get event by id 
     * Funció que retorna l'event amb el id sol·licitat
     * @throws java.lang.Exception
     * @param id String
     * @return type objecte event amb el id demanat
     */
    public Event getEventById(String id) throws Exception {
        String qry = "select * from events where id = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, id);
        return findUniqueResult(preparedStatement);
    }
    
    /**
     * Create Event
     * Funció que crea un event
     * @param event Event
     * @throws java.lang.Exception
     * @return type objecte event creat
     */
    public Event addEvent(Event event) throws Exception {
        String qry = "INSERT INTO events (event_title, event_startDate, event_finishDate, event_type, event_description, event_address, event_city, users_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = getPreparedStatement(qry);

        preparedStatement.setString(1, event.getEvent_Title());
        preparedStatement.setDate(2, event.getEvent_startDate());
        preparedStatement.setDate(3, event.getEvent_finishDate());
        preparedStatement.setString(4, event.getEvent_type());
        preparedStatement.setString(5, event.getEvent_description());
        preparedStatement.setString(6, event.getEvent_address());
        preparedStatement.setString(7, event.getEvent_city());       
        preparedStatement.setInt(8, event.getUsers_id());
        
        int result = executeUpdateQuery(preparedStatement);
        if (result == 0) {
            throw new Exception("Error creating event");
        }
        return getEventById(getIdByTitleStartandEndEvent(event.getEvent_Title(), event.getEvent_startDate(), event.getEvent_finishDate()));
    }
    
    /**
     * Get Id By Title Start and End Event
     * Funció que cerca un event amb un titol, una hora de començament i finalització concrets i en retorna el seu ID
     * @param title String
     * @param startEvent LocalDateTime
     * @param finishEvent LocalDateTime
     * @throws java.lang.Exception
     * @return type String id event
     */
    public String getIdByTitleStartandEndEvent(String title, Date startEvent, Date finishEvent) throws Exception{
        String qry = "select * from events where event_title = ? AND event_startDate = ? AND event_finishDate = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        
        preparedStatement.setString(1, title);
        preparedStatement.setDate(2, startEvent);
        preparedStatement.setDate(3, finishEvent);
        Event event = findUniqueResult(preparedStatement);
        return String.valueOf(event.getId());
    }
    
    /**
     * Update event
     * Funció que modifica un event amb id determinat
     * @param event Object
     * @param idEvent String
     * @throws java.lang.Exception
     * @return type objecte event modificat
     */
    public Event updateEvent(Event event, String idEvent) throws Exception {

        String qry =  "UPDATE events SET event_title = ?, event_startdate = ?, event_finishdate = ?, event_type = ?, event_description = ?, event_address = ?, event_city = ?, users_id = ?  WHERE id = ? ";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, event.getEvent_Title());
        preparedStatement.setDate(2, event.getEvent_startDate());
        preparedStatement.setDate(3, event.getEvent_finishDate());
        preparedStatement.setString(4, event.getEvent_type());
        preparedStatement.setString(5, event.getEvent_description());
        preparedStatement.setString(6, event.getEvent_address());
        preparedStatement.setString(7, event.getEvent_city());
        preparedStatement.setInt(8, event.getUsers_id());
        preparedStatement.setString(9, idEvent);
        return createOrUpdateEvent(idEvent, preparedStatement);
    }
    
    /**
     * Delete event 
     * Funció que elimina un event
     * @param id String
     * @throws java.lang.Exception
     */
    public void deleteEvent(String id) throws Exception {
        String qry = "DELETE FROM events WHERE id = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, id);
        createOrUpdateEvent(id, preparedStatement);
    }
    
    // ----------     Métodes pels filtres  -------------------------------------------------------------------------------
    
    /**
     * Get Events By Municipi
     * Funció que retorna els events que contenen un municipi determinat dins la taula events de la BD
     * @param municipi String
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events que contenen un determinat municipi de la BD
     */
    public List<Event> getEventsByMunicipi(String municipi) throws SQLException {
        String qry = "select * from events where event_city LIKE ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, "%" + municipi.toLowerCase() + "%");
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    /**
     * Get Events By Data
     * Funció que retorna els events d'una data determinada de dins la taula events de la BD
     * @param from Date
     * @param to Date
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events d'una determinada data de la BD
     */
    public List<Event> getEventsByData(String from, String to) throws SQLException {
        String qry = "select * from events where event_startDate >= ? and event_finishDate <= ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, from);
        preparedStatement.setString(2, to);
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    /**
     * Get Events By Nom
     * Funció que retorna els events que contenen un nom determinat de dins la taula events de la BD
     * @param nom String
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events que contenen un nom determinat de la BD
     */
    public List<Event> getEventsByNom(String nom) throws SQLException {
        String qry = "select * from events where event_title LIKE ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, "%" + nom.toLowerCase() + "%");
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }

    /**
     * Get Events By Data and Municipi
     * Funció que retorna els events que corresponen a una data determinada i contenen un municipi determinat de dins la taula events de la BD
     * @param from Date
     * @param to Date
     * @param municipi String
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events que corresponen a una data determinada i contenen un municipi determinat de dins de la BD
     */
    public List<Event> getEventsByDataandMunicipi(String from, String to, String municipi) throws SQLException {
        String qry = "select * from events where event_startDate >= ? AND event_finishDate <= ? AND event_city LIKE ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, from);
        preparedStatement.setString(2, to);
        preparedStatement.setString(3, "%" + municipi.toLowerCase() + "%");
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    /**
     * Get Events By Nom and Data
     * Funció que retorna els events que contenen un nom d'event determinat i corresponen a una data determinada de dins la taula events de la BD
     * @param nom String
     * @param from Date
     * @param to Date
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events que contenen un nom d'event determinat i corresponen a una data determinada de dins de la BD
     */
    public List<Event> getEventsByNomandData(String nom, String from, String to) throws SQLException {
        String qry = "select * from events where event_title LIKE ? AND event_startDate >= ? AND event_finishDate <= ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, "%" + nom.toLowerCase() + "%");
        preparedStatement.setString(2, from);
        preparedStatement.setString(3, to);
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    /**
     * Get Events By Nom and Municipi
     * Funció que retorna els events que contenen un nom d'event determinat i contenen un determinat municipi de dins la taula events de la BD
     * @param nom String
     * @param municipi String
     * @throws java.sql.SQLException
     * @return type List ArrayList amb els events que contenen un nom d'event determinat i contenen un determinat municipi de dins de la BD
     */
    public List<Event> getEventsByNomandMunicipi(String nom, String municipi) throws SQLException {
        String qry = "select * from events where event_title LIKE ? AND event_city LIKE ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, "%" + nom.toLowerCase() + "%");
        preparedStatement.setString(2, "%" + municipi.toLowerCase() + "%");
        List<Event> events = executeQuery(preparedStatement);
        return events;
    }
    
    // ----------     Métodes auxiliars -------------------------------------------------------------------------------
    
    private Event createOrUpdateEvent(String id, PreparedStatement preparedStatement) throws Exception {
        int result = executeUpdateQuery(preparedStatement);
        if (result == 0) {
            throw new Exception("Error creating event");
        }
        return getEventById(id);
    }
    
    
    private Event findUniqueResult(PreparedStatement preparedStatement) throws Exception {
        List<Event> events = executeQuery(preparedStatement);
        if (events.isEmpty()) {
            return null;
        }
        if (events.size() > 1) {
            throw new Exception("Only one result expected");
        }
        return events.get(0);
    }

    private List<Event> executeQuery(PreparedStatement preparedStatement) {
        List<Event> events = new ArrayList<>();

        try (
                ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Event event = buildEventFromResultSet(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    private PreparedStatement getPreparedStatement(String query) throws SQLException {
        if (getConnection() == null) {
            try {
                setConnection(dBConnection.getConnection());
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return getConnection().prepareStatement(query);
    }


    private int executeUpdateQuery(PreparedStatement preparedStatement) {
        int result = 0;
        if (getConnection() == null) {
            try {
                setConnection(dBConnection.getConnection());
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private Event buildEventFromResultSet(ResultSet rs) throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        int eventId = rs.getInt("id");
        String event_title = rs.getString("event_title");
        Date event_startDate = rs.getDate("event_startDate");
        Date event_finishDate = rs.getDate("event_finishDate");
        String event_type = rs.getString("event_type");
        String event_description = rs.getString("event_description");
        String event_address = rs.getString("event_address");
        String event_city = rs.getString("event_city");
        LocalDateTime event_registered = rs.getTimestamp("event_registered").toLocalDateTime();
        int users_id = rs.getInt("users_id");
        
        Event event = new Event(eventId, event_title, event_startDate, event_finishDate, event_type, event_description, event_address, event_city, event_registered, users_id);
        return event;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
