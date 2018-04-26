/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.Event;
import domain.repository.EventDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.Variables;


@Path("/events")

/**
 *
 * @author Albert
 */
public class EventJSONService {
    
    //UserDAO userDAO = new UserDAO();
    Variables variables = new Variables();
    EventDAO eventDAO = new EventDAO();
    
    // Test - OK 
    // GET - getEventById URL -> http://localhost:8080/eventfest/rest/events/query
    // Parameters -> id y token     ejemplo  -> ?id=2&token=257eedab81802476cd226bb9cb592f2b
    
    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventById(@QueryParam("id") int id, @QueryParam("token") String token) throws Exception {
        if(variables.checkToken(token)){
            Event event = this.eventDAO.getEventById(id);
            if(event == null){
                return Response.ok("{\"missatge\" : \"No existeix l'event amb ID " + id + "\"}", MediaType.APPLICATION_JSON).build();
            }else{
                return Response.ok("{\"event\" : {" + event.eventInString(event) + "}, \"missatge\" : \"event amb ID " + id + "\"}", MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();    
    }
    
    // Test - OK 
    // GET - countEvents URL -> http://localhost:8080/eventfest/rest/events/count
    // Parameters -> token     ejemplo  -> ?token=257eedab81802476cd226bb9cb592f2b
    
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countEvents(@QueryParam("token") String token) throws SQLException, Exception {
        if(variables.checkToken(token)){
            int resultat = eventDAO.countEvents();
            return Response.ok("{\"missatge\" : \""+resultat+"\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
        
    }
    
    // Test - OK 
    // GET - getByFilter URL -> http://localhost:8080/eventfest/rest/events/getByFilter
    // Parameters -> token, nom, data i municipi     ejemplo  -> ?token=555f8d875307f0deb7c982772163c0c6&nom=qualsevol nom&data=2018-03-24&municipi=BARCELONA
    
    @GET
    @Path("/getByFilter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByFilter(@QueryParam("token") String token, @QueryParam("nom") String nom, @QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("municipi") String municipi) throws SQLException, Exception {
        if(variables.checkToken(token)){
            if(nom.equals("")&&from.equals("")&&to.equals("")&&municipi.equals("")){
                return Response.ok(this.eventDAO.getAllEvents(), MediaType.APPLICATION_JSON).build();
            }
            if(nom.equals("")&&from.equals("")&&to.equals("")&&!municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByMunicipi(municipi), MediaType.APPLICATION_JSON).build();
            }
            if(nom.equals("")&&!from.equals("")&&!to.equals("")&&municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByData(from,to), MediaType.APPLICATION_JSON).build();
            }
            if(!nom.equalsIgnoreCase("")&&from.equals("")&&to.equals("")&&municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByNom(nom), MediaType.APPLICATION_JSON).build();
            }
            if(nom.equalsIgnoreCase("")&&!from.equals("")&&!to.equals("")&&!municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByDataandMunicipi(from, to, municipi), MediaType.APPLICATION_JSON).build();
            }
            if(!nom.equalsIgnoreCase("")&&!from.equals("")&&!to.equals("")&&municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByNomandData(nom, from, to), MediaType.APPLICATION_JSON).build();
            }
            if(!nom.equalsIgnoreCase("")&&from.equals("")&&to.equals("")&&!municipi.equals("")){
                return Response.ok(this.eventDAO.getEventsByNomandMunicipi(nom, municipi), MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.ok("", MediaType.APPLICATION_JSON).build();
        
    }
    
    // Test - OK 
    // POST - addEvent URL -> http://localhost:8080/eventfest/rest/events/create
    // Pattern json in > {"event_title": "Fira ambulant","event_startDate": "2018-02-14","event_finishDate": "2018-02-14","event_type": "Fira","event_description": "Fira als carrers de Barcelona. Es presentaran els productes locals.","event_address": "passeig de l'havana","users_id": 5}
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEvent(Event event, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                this.eventDAO.addEvent(event);
                String missatge = "\"missatge\" : \"Afegit l'event correctament\"";
                return Response.ok("{"+missatge+"}", MediaType.APPLICATION_JSON).build(); 
            }
        } catch (Exception ex) {
            Logger.getLogger(EventJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut crear l'event\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
    }
    
    // Test - OK 
    // PUT - updateEvent URL -> http://localhost:8080/eventfest/rest/events/update
    // Pattern json in > {"event_title": "Fira ambulant","event_startDate": "2018-02-14","event_finishDate": "2018-02-14","event_type": "Fira","event_description": "Fira als carrers de Barcelona. Es presentaran els productes locals.","event_address": "passeig de l'havana","users_id": 5}
    // Parameters -> id y token     ejemplo  -> ?id=2&token=257eedab81802476cd226bb9cb592f2b

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvent(Event event, @QueryParam("id") String id, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                return Response.ok(this.eventDAO.updateEvent(event, id), MediaType.APPLICATION_JSON).build();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut modificar l'event\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
    }
    
    // Test - OK 
    // DELETE - deleteEvent URL -> http://localhost:8080/eventfest/rest/events/delete
    // Parameters -> id y token     ejemplo  -> ?id=2&token=257eedab81802476cd226bb9cb592f2b&login=albert
    
    @DELETE
    @Path("/delete")
    public Response deleteEvent(@QueryParam("id") String id, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                this.eventDAO.deleteEvent(id);
                String missatge = "\"missatge\" : \"Eliminació de l'event correcte\"";
                
                return Response.ok("{"+missatge+"}", MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut eliminar l'event\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
    }
}
