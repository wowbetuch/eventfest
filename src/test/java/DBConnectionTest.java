
import domain.repository.DBConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {
    DBConnection dBConnection;
    Connection connection;
    private String connectionProperties = "db.properties";

    @Before
    public void setUp(){
        dBConnection = new DBConnection(connectionProperties);
    }

    @After
    public void cleanUp() throws SQLException {
        if(connection != null){
            connection.close();   
        } 
    }

    @Test
    //Connexió amb la BD correctament
    public void connectarAmbLaBaseDeDades() throws IOException, SQLException {
        connection = dBConnection.getConnection();
        Assert.assertEquals("MySQL Connector Java", connection.getMetaData().getDriverName());
        Assert.assertEquals("eventfest", connection.getCatalog());
    }
}
    