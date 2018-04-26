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
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    private int id;
    private String user_login;
    private String user_pass;
    private String user_email;
    private LocalDateTime user_registered;
    private int user_role;

    public User() {
    }
    
    public User(int id, String user_login, String user_pass, String user_email, LocalDateTime user_registered, int user_role) {
        this.id = id;
        this.user_login = user_login;
        this.user_pass = user_pass;
        this.user_email = user_email;
        this.user_registered = user_registered;
        this.user_role = user_role;
    }
    
    public User(String id, String user_login, String user_pass, String user_email, String user_registered, int user_role){
        this.id = Integer.parseInt(id);
        this.user_login = user_login;
        this.user_pass = user_pass;
        this.user_email = user_email;
        this.user_registered=LocalDateTime.parse(user_registered);
        this.user_role = user_role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return user_login;
    }

    public void setLogin(String user_login) {
        this.user_login = user_login;
    }

    public String getPass() {
        return user_pass;
    }

    public void setPass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getEmail() {
        return user_email;
    }

    public void setEmail(String user_email) {
        this.user_email = user_email;
    }
    
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    public LocalDateTime getUser_Registered() {
        return user_registered;
    }

    public void setUser_Registered(LocalDateTime user_registered) {
        this.user_registered = user_registered;
    }
    public int getRole() {
        return user_role;
    }

    public void setRole(int user_role) {
        this.user_role = user_role;
    }
    public String userInString(User user){
        return "\"id\" : \""+user.getId()+"\", \"user_login\" : \""+user.getLogin()+"\", \"user_email\" : \""+user.getEmail()+"\", \"user_registered\" : \""+user.getUser_Registered()+"\", \"user_role\" : "+user.getRole();
    }
}
