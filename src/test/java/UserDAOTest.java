/*
 * Classe de proves per tal de provar el funcionament de la classe UserDAO sobre la BD MySQL d'EventFest
 */

/**
 *
 * @author Albert
 */

import domain.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import domain.repository.DBConnection;
import domain.repository.UserDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//Tests de cadascun dels métodes usats pel servei i que consulten o modifiquen la base de dades
public class UserDAOTest {

    private DBConnection dBConnection;
    private String connectionProperties = "db_test.properties";
    UserDAO userDAO;

    @Before
    public void setUp() {
        //Establim connexió amb la BD
        dBConnection = new DBConnection(connectionProperties);
        userDAO = new UserDAO(dBConnection);
    }

    @After
    public void tearDown() throws IOException, SQLException {
        //Tanquem la connexió amb la BD
        userDAO.getConnection().close();
    }

    @Test
    //test que compta el nombre d'usuaris dins la base de dades
    public void countUsers() throws Exception {
        int users = userDAO.countUsers();
        Assert.assertEquals("Hauriem de tenir 5 usuaris a la base de dades", 5, users);
    }
    
    @Test
    //test per saber si el métode detecta que un usuari es troba dins la base de dades i permetre el login 
    public void getValidUser() throws Exception {
        String existingUserLogin = "albert";
        String unknownUserLogin = "unknown";
        String existingAdminUserLogin = "admin";
        String existingAdminUserPass = "21232f297a57a5a743894a0e4a801fc3";
        String existingUserPass = "62608e08adc29a8d6dbc9754e659f125";
        String unknownUserPass = "unknown";

        int missatge = userDAO.getValidUser(existingUserLogin,existingUserPass);
        //verifiquem detecta l'usuari com a usuari general
        Assert.assertEquals(2, missatge);
        missatge = userDAO.getValidUser(existingAdminUserLogin,existingAdminUserPass);
        //verifiquem detecta l'usuari com a usuari admin
        Assert.assertEquals(1, missatge);
        //verifiquem no troba l'usuari (codi 8 no valid)
        missatge = userDAO.getValidUser(unknownUserLogin,unknownUserPass);
        Assert.assertEquals(8, missatge);
    }
    
    @Test
    //test que obté tots els usuaris dins la base de dades
    public void getAllUsers() throws SQLException {
        List<User> users = userDAO.getAllUsers();
        //comprovem la mida de la llista amb el nombre a BD
        Assert.assertEquals("Hauriem de tenir 5 usuaris a la base de dades", 5, users.size());
    }

    @Test
    public void getUserByEmail() throws Exception {
        String existingEmail = "albert@ioc.xtec.com";
        String unknownEmail = "does.not@exist.com";

        User user = userDAO.getUserByEmail(existingEmail);
        //Verifiquem obté l'usuari amb un correu determinat
        Assert.assertNotNull(user);
        user = userDAO.getUserByEmail(unknownEmail);
        //Verifiquem no obté cap usuari ja que l'id no existeix
        Assert.assertNull(user);
    }

    @Test
    public void getUserByUserLogin() throws Exception {
        String existingUserLogin = "albert";
        String unknownUserLogin = "unknown";

        User user = userDAO.getUserByUserLogin(existingUserLogin);
        //Verifiquem obté l'usuari amb un login determinat
        Assert.assertNotNull(user);
        user = userDAO.getUserByUserLogin(unknownUserLogin);
        //Verifiquem no obté cap usuari ja que el login no existeix
        Assert.assertNull(user);
    }

    @Test
    public void addUser() throws Exception {
        String userLogin = "testUser";
        String userPass = "Pete Test";
        String userEmail = "pete@email.com";
        int userRole = 2;
        User createdUser = userDAO.addUser(userLogin, userPass, userEmail, userRole);
        //Verifiquem s'ha creat l'usuari
        Assert.assertNotNull(createdUser);
        //Verifiquem que el login de l'usuari creat i el de la BD coincideix
        Assert.assertEquals(userLogin, createdUser.getLogin());
        //Verifiquem que el seu id no és 0
        Assert.assertNotEquals(0, createdUser.getId());
        
        //fer que l'elimini per a que la bateria de tests continui
        userDAO.deleteUser(createdUser.getLogin());
    }

    /*@Test
    public void updateUserEmail() throws Exception {
        String userLogin = "testUser";
        String userPass = "Pete Test";
        String userEmail = "pete@email.com";
        int userRole = 2;
        User createdUser = userDAO.addUser(userLogin, userPass, userEmail, userRole);
        //Verifiquem s'ha creat l'usuari
        Assert.assertNotNull(createdUser);
        //Verifiquem que el email de l'usuari creat i el de la BD coincideix
        Assert.assertEquals(userEmail, createdUser.getEmail());
        //Fem el canvi de l'email
        User updatedUser = userDAO.updateUserEmail(createdUser, "new@email.com");
        //Verifiquem que el id de l'usuari creat i el de la BD modificat coincideix
        Assert.assertEquals(createdUser.getId(), updatedUser.getId());
        //Verifiquem que el nou email de l'usuari creat i el de la BD coincideix
        Assert.assertEquals("new@email.com", updatedUser.getEmail());
        
        //fer que l'elimini per a que la bateria de tests continui
        userDAO.deleteUser(createdUser.getLogin());
    }*/

    @Test
    public void deleteUser() throws Exception {
        String userLogin = "testUser";
        String userPass = "Pete Test";
        String userEmail = "pete@email.com";
        int userRole = 3;
        User createdUser = userDAO.addUser(userLogin, userPass, userEmail, userRole);
        //Verifiquem s'ha creat l'usuari
        Assert.assertNotNull(createdUser);
        //eliminem l'usuari
        userDAO.deleteUser(createdUser.getLogin());
        //Intentem obtenir de nou l'usuari eliminat
        User deletedUser = userDAO.getUserByUserLogin(userLogin);
        //Verifiquem que l'usuari no s'ha trobat
        Assert.assertNull(deletedUser);
    }


}

