/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.repository;

import domain.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Albert
 */
public class UserDAO {
    
    private DBConnection dBConnection;
    private Connection connection;
    //Map amb els usuaris que es troben a la sessió de connexió al servidor
    private Map<String, String> usuarisSessio = new HashMap<>();
    
    public UserDAO(DBConnection dBConnection) {
        this.dBConnection = dBConnection;
    }
    
    
    

    public UserDAO() {
        try {
            dBConnection = (DBConnection) new InitialContext().lookup("java:global/eventfest/DBConnection");
            dBConnection.setConnectionFile("db.properties");
        } catch (NamingException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    /**
     * Get valid user
     * Funció que retorna en un String(adaptat a JSON) el rol de l'usuari i el missatge associat
     * @throws java.lang.Exception
     * @param user_login String
     * @param user_pass String
     * @return type String dades de validació de l'usuari
     */
    public int getValidUser(String user_login, String user_pass) throws Exception{
        //Posem 8 com a codi de rol no valid
        int rol = 8;
        String qry = "select * from users where user_login = ? AND user_pass = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, user_login);
        preparedStatement.setString(2, user_pass);
        User resultat = findUniqueResult(preparedStatement);
        if(resultat!=null) {
            rol = resultat.getRole();
        }
        
        return rol;
    }
    
    /**
     * Get user by Id
     * Funció que retorna les dades de l'usuari corresponent al Id associat
     * @throws java.lang.Exception
     * @param user_id Int
     * @return type objecte user amb l'ID demanat
     */
    public User getUserById(int user_id) throws Exception{
        String qry = "select * from users where id = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setInt(1, user_id);
        return findUniqueResult(preparedStatement);
    }
    
    /**
     * Count users
     * Funció que retorna el nombre de registres dins la BD
     * @throws java.lang.Exception
     * @return type int nombre registres
     */
    public int countUsers() throws Exception{
        //Por defecto no hay resultados       
        int n=0;
        String qry = "SELECT count(*) FROM users";
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
     * Get All users
     * Funció que retorna tots els usuaris dins la taula users de la BD
     * @throws java.sql.SQLException
     * @return type List ArrayList amb tots els usuaris de la BD
     */
    public List<User> getAllUsers() throws SQLException {
        String qry = "select id, user_login, user_pass, user_email, user_registered, user_role from users";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        List<User> users = executeQuery(preparedStatement);
        return users;
    }
    
    /**
     * Find user by Email
     * Funció que retorna l'usuari amb l'email sol·licitat
     * @throws java.lang.Exception
     * @param userEmail String
     * @return type objecte user amb l'email demanat
     */
    public User getUserByEmail(String userEmail) throws Exception {
        String qry = "select * from users where user_email = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, userEmail);
        return findUniqueResult(preparedStatement);
    }
    
    /**
     * Find user by user login
     * Funció que retorna l'usuari amb el login d'usuari sol·licitat
     * @throws java.lang.Exception
     * @param userlogin String
     * @return type objecte user amb el login demanat
     */
    public User getUserByUserLogin(String userlogin) throws Exception {
        String qry = "select * from users where user_login =?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, userlogin);
        return findUniqueResult(preparedStatement);
    }
    
    /**
     * Create user
     * Funció que crea un usuari
     * @param userLogin String
     * @param userPass String
     * @param userEmail String
     * @param userRole int
     * @throws java.lang.Exception
     * @return type objecte user creat
     */
    public User addUser(String userLogin, String userPass, String userEmail, int userRole) throws Exception {
        String qry = "INSERT INTO users (user_login, user_pass, user_email, user_role) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, userLogin);
        preparedStatement.setString(2, userPass);
        preparedStatement.setString(3, userEmail);
        preparedStatement.setInt(4, userRole);
        return createOrUpdateUser(userLogin, preparedStatement);
    }
    
    /**
     * Update user Email
     * Funció que modifica l'email d'un usuari
     * @param user Object
     * @param newEmail String
     * @throws java.lang.Exception
     * @return type objecte user modificat
     */
    public User updateUser(User user, String id) throws Exception {
        String qry =  "UPDATE users SET user_login = ?, user_pass = ?, user_email = ?, user_role = ?  WHERE id = ? ";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPass());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setInt(4, user.getRole());
        preparedStatement.setString(5, id);
        return createOrUpdateUser(id, preparedStatement);
    }
    
    /**
     * Delete user 
     * Funció que elimina un usuari
     * @param login String
     * @throws java.lang.Exception
     */
    public void deleteUser(String login) throws Exception {
        String qry = "DELETE FROM users WHERE user_login = ?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, login);
        createOrUpdateUser(login, preparedStatement);
    }
    
    // ----------     Métodes auxiliars -------------------------------------------------------------------------------
    
    /*public void addUserSession(String login, String token){
        usuarisSessio.put(login,token);
        System.out.print(usuarisSessio.values());
    }
    
    public void removeUserSession(String login, String token){
        usuarisSessio.remove(login,token);
    }
    
    public boolean checkToken (String token){
        System.out.print(usuarisSessio.values());
        return usuarisSessio.containsValue(token); //nombreMap.get(K clave);
    }
    
    public void addUserSession(String login, String token) throws SQLException, Exception{
        String qry = "UPDATE users SET user_token = ?  WHERE user_login = ? ";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, token);
        preparedStatement.setString(2, login);
        createOrUpdateUser(login, preparedStatement);
    }
    
    public void removeUserSession(String login, String token) throws SQLException, Exception{
        String qry = "UPDATE users SET user_token = ''  WHERE user_login = ? ";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, login);
        createOrUpdateUser(login, preparedStatement);
    }
    
    public boolean checkToken (String token) throws SQLException, Exception{
        String qry = "select * from users where user_token =?";
        PreparedStatement preparedStatement = getPreparedStatement(qry);
        preparedStatement.setString(1, token);
        User user = findUniqueResult(preparedStatement);
        return user != null;
    }*/
    private User createOrUpdateUser(String userlogin, PreparedStatement preparedStatement) throws Exception {
        int result = executeUpdateQuery(preparedStatement);
        if (result == 0) {
            throw new Exception("Error creating user");
        }
        return getUserByUserLogin(userlogin);
    }
    
    
    private User findUniqueResult(PreparedStatement preparedStatement) throws Exception {
        List<User> users = executeQuery(preparedStatement);
        if (users.isEmpty()) {
            return null;
        }
        if (users.size() > 1) {
            throw new Exception("Only one result expected");
        }
        return users.get(0);
    }

    private List<User> executeQuery(PreparedStatement preparedStatement) {
        List<User> users = new ArrayList<>();

        try (
                ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                User user = buildUserFromResultSet(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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
    
    private User buildUserFromResultSet(ResultSet rs) throws SQLException{
        int userId = rs.getInt("id");
        String user_login = rs.getString("user_login");
        String user_pass = rs.getString("user_pass");
        String user_email = rs.getString("user_email");
        LocalDateTime user_registered = rs.getTimestamp("user_registered").toLocalDateTime();
        int user_role = rs.getInt("user_role");
        //String user_token = rs.getString("user_token");
        
        User user = new User(userId, user_login, user_pass, user_email, user_registered, user_role);
        return user;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
