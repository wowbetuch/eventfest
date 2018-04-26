/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Albert
 */

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    private int id;
    private String event_title;
    private Date event_startDate;
    private Date event_finishDate;
    private String event_type;
    private String event_description;
    private String event_address;
    private String event_city;
    private LocalDateTime event_registered;
    private int users_id;

    public Event() {
    }
    
    public Event(int id, String event_title, Date event_startdate, Date event_finishdate, String event_type, String event_description, String event_address, String event_city, LocalDateTime event_registered, int users_id) {
        this.id = id;
        this.event_title = event_title;
        this.event_startDate = event_startdate;
        this.event_finishDate = event_finishdate;
        this.event_type = event_type;
        this.event_description = event_description;
        this.event_address = event_address;
        this.event_city = event_city;
        this.event_registered = event_registered;
        this.users_id = users_id;
    }
    
    public Event(String id, String event_title, String event_startdate, String event_finishdate, String event_type, String event_description, String event_address, String event_city, String event_registered, int users_id){
        this.id = Integer.parseInt(id);
        this.event_title = event_title;
        this.event_startDate=parseFecha(event_startdate);
        this.event_finishDate=parseFecha(event_finishdate);
        this.event_type = event_type;
        this.event_description = event_description;
        this.event_address = event_address;
        this.event_city = event_city;
        this.event_registered=LocalDateTime.parse(event_registered);
        this.users_id = users_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent_Title() {
        return event_title;
    }

    public void setEvent_Title(String event_title) {
        this.event_title = event_title;
    }

    public Date getEvent_startDate() {
        return event_startDate;
    }

    public void setEvent_startDate(Date event_startDate) {
        this.event_startDate = event_startDate;
    }
    
    public Date getEvent_finishDate() {
        return event_finishDate;
    }

    public void setEvent_finishDate(Date event_finishDate) {
        this.event_finishDate = event_finishDate;
    }
    
    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
    
    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_address() {
        return event_address;
    }

    public void setEvent_address(String event_address) {
        this.event_address = event_address;
    }
    
    public String getEvent_city() {
        return event_city;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }
    
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    public LocalDateTime getEvent_Registered() {
        return event_registered;
    }

    public void setUser_Registered(LocalDateTime event_registered) {
        this.event_registered = event_registered;
    }
    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }
    
    public String eventInString(Event event){
        return "\"id\" : \""+event.getId()+"\", \"event_title\" : \""+event.getEvent_Title()+"\", \"event_startDate\" : \""+event.getEvent_startDate()+"\", \"event_finishDate\" : \""+event.getEvent_finishDate()+"\", \"event_type\" : \""+event.getEvent_type()+"\", \"event_description\" : \""+event.getEvent_description()+"\", \"event_address\" : \""+event.getEvent_address()+"\", \"event_city\" : \""+event.getEvent_city()+"\", \"event_registered\" : \""+event.getEvent_Registered()+"\", \"users_id\" : "+event.getUsers_id();
    }
    
    /**
     * Permite convertir un String en fecha (Date).
     * @param fecha Cadena de fecha dd/MM/yyyy
     * @return Objeto Date
     */
    public static Date parseFecha(String fecha)
    {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = (Date) formato.parse(fecha);
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
        return fechaDate;
    }
    
    /**
     * Permite convertir un String en fecha (Date).
     * @param fecha Cadena de fecha dd/MM/yyyy
     * @return Objeto Date
     */
    public static String stringFecha(Date fecha)
    {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaDate = null;
        fechaDate = formato.format(fecha);
  
        return fechaDate;
    }
}
