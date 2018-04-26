/*
 * Web service of users to be consumed by Desktop/Mobile App EventFest 
 */

/**
 * @author Albert
 * @version 2018/03/01/
 */

package service;

import domain.User;
import domain.repository.UserDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;
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


@Path("/users")
public class UserJSONService {
    
    UserDAO userDAO = new UserDAO();    
    Variables variables = new Variables();
    // Test - OK 
    // GET - checkUser URL -> http://localhost:8080/eventfest/rest/users/connect
    // Parameters -> login y pass codificado md5     ejemplo  -> ?user_login=albert&user_pass=62608e08adc29a8d6dbc9754e659f125
    
    @GET
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response connectUser(@QueryParam("login") String login, @QueryParam("pass") String pass) {
        try {
            String missatge;
            String resultat;
            int rol = userDAO.getValidUser(login, pass);
            switch(rol){
                case 0:
                    missatge = ", \"user_role\":0, \"missatge\": \"L'usuari és superadministrador\"";
                    break;
                case 1:
                    missatge = ", \"user_role\":1, \"missatge\": \"L'usuari és administrador\"";
                    break;
                case 2:
                    missatge = ", \"user_role\":2, \"missatge\": \"L'usuari és usuari general\"";
                    break;
                default:
                    missatge = "\"user_role\":\"\", \"missatge\": \"L'usuari no es troba al sistema\"";
            }
            if((rol==8)){
                return Response.ok("{" + missatge + "}", MediaType.APPLICATION_JSON).build();
            }else{
                String token = md5(login + LocalDateTime.now().toString());
                variables.addUserSession(login , token);
                resultat = "{\"token\":\"" + token + "\"" + missatge + "}";
                return Response.ok(resultat, MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Test - OK 
    // GET - disconnectUserByLogin URL -> http://localhost:8080/eventfest/rest/users/disconnect
    // Parameters -> login y token     ejemplo  -> ?login=albert&token=7a2c857f02f5f7710219c502812ffb9b
    
    @GET
    @Path("/disconnect")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disconnectUserByLogin(@QueryParam("login") String login, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                variables.removeUserSession(login, token);
                return Response.ok("{\"missatge\" : \"Usuari " + login + " desconnectat\"}", MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"L'usuari no es valid\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"El token no es valid\"}", MediaType.APPLICATION_JSON).build();
    }
    
    // Test - OK 
    // GET - getUserById URL -> http://localhost:8080/eventfest/rest/users/query
    // Parameters -> id y token     ejemplo  -> ?id=2&token=257eedab81802476cd226bb9cb592f2b
    
    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@QueryParam("id") int id, @QueryParam("token") String token) throws Exception {
        if(variables.checkToken(token)){
            User usuari = this.userDAO.getUserById(id);
            if(usuari == null){
                return Response.ok("{\"missatge\" : \"No existeix l'usuari amb ID " + id + "\"}", MediaType.APPLICATION_JSON).build();
            }else{
                return Response.ok("{\"user\" : {" + usuari.userInString(usuari) + "}, \"missatge\" : \"usuari amb ID " + id + "\"}", MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();    
    }
    
    // Test - OK 
    // GET - countUsers URL -> http://localhost:8080/eventfest/rest/users/countusers
    // Parameters -> token     ejemplo  -> ?token=257eedab81802476cd226bb9cb592f2b
    
    @GET
    @Path("/countusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countUsers(@QueryParam("token") String token) throws SQLException, Exception {
        if(variables.checkToken(token)){
            int resultat = this.userDAO.countUsers();
            return Response.ok("{\"missatge\" : \""+resultat+"\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
        
    }
    
    // Test - OK 
    // GET - getAllUsers URL -> http://localhost:8080/eventfest/rest/users/
    // Parameters -> token     ejemplo  -> ?token=257eedab81802476cd226bb9cb592f2b
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@QueryParam("token") String token) throws SQLException, Exception {
        if(variables.checkToken(token)){
            return Response.ok(this.userDAO.getAllUsers(), MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
        
    }
    
    // Test - OK 
    // POST - addUser URL -> http://localhost:8080/eventfest/rest/users/create
    // Pattern json in > {"user_login" : "newuser", "user_pass" : "password", "user_email" : "newuser@ioc.com", "user_role" : 2}
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        try {
            this.userDAO.addUser(user.getLogin(), user.getPass(), user.getEmail(), user.getRole());
            String missatge = "\"missatge\" : \"Afegit l'usuari correctament\"";
            return Response.ok("{"+missatge+"}", MediaType.APPLICATION_JSON).build();            
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut crear l'usuari\"}", MediaType.APPLICATION_JSON).build();
        }
    }
    
    // Test - OK 
    // PUT - updateUserEmail URL -> http://localhost:8080/eventfest/rest/users/modify
    // Pattern json in > {"user_login" : "newuser", "user_pass" : "password", "user_email" : "newuser@ioc.com", "user_role" : 2, "user_token" : ""}
    // Parameters -> email y token     ejemplo  -> ?email=albert@xtec.com&token=257eedab81802476cd226bb9cb592f2b

    @PUT
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user, @QueryParam("id") String id, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                return Response.ok(this.userDAO.updateUser(user, id), MediaType.APPLICATION_JSON).build();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut modificar l'usuari\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
    }
    
    // Test - OK 
    // PUT - deleteUser URL -> http://localhost:8080/eventfest/rest/users/remove
    // Parameters -> login y token     ejemplo  -> ?login=albert&token=257eedab81802476cd226bb9cb592f2b&login=albert
    
    @DELETE
    @Path("/remove")
    public Response deleteUser(@QueryParam("login") String login, @QueryParam("token") String token) {
        try {
            if(variables.checkToken(token)){
                this.userDAO.deleteUser(login);
                String missatge = "\"missatge\" : \"Eliminació de l'usuari correcta\"";
                
                return Response.ok("{"+missatge+"}", MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserJSONService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("{\"missatge\" : \"No s'ha pogut eliminar l'usuari\"}", MediaType.APPLICATION_JSON).build();
        }
        return Response.ok("{\"missatge\" : \"Token no valid\"}", MediaType.APPLICATION_JSON).build();
    }
    
    // -------------Metodes de codificacio md5 i SHA1---------------------------------------------------
    
    
    /**
     * Codificació de userDAOs string amb métodes md5 o sha1
     * @param txt, text in plain format
     * @param hashType MD5 OR SHA1
     * @return hash in hashType 
     */
    public static String getHash(String txt, String hashType) {
        try {
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                    byte[] array = md.digest(txt.getBytes());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < array.length; ++i) {
                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                 }
                    return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                //error action
            }
            return null;
    }

    public static String md5(String txt) {
        return getHash(txt, "MD5");
    }

    public static String sha1(String txt) {
        return getHash(txt, "SHA1");
    }
}
