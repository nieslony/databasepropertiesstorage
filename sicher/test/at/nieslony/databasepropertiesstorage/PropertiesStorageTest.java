/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.nieslony.databasepropertiesstorage;

import at.nieslony.utils.DbUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author claas
 */
public class PropertiesStorageTest {
    static Connection con;

    public PropertiesStorageTest() {
    }

    @BeforeClass
    public static void setUpClass()
            throws ClassNotFoundException, SQLException,
            FileNotFoundException, IOException
    {
        System.out.println("Opening database...");
        Class.forName("org.postgresql.Driver");
        con = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/spielwiese",
                        "tester", "tester");
        con.setAutoCommit(false);
        System.out.println("... success.");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp()
            throws SQLException, IOException
    {
        String sql;
        Statement stm = con.createStatement();

        sql = "DROP SCHEMA IF EXISTS public CASCADE ;";
        stm.execute(sql);
        sql = "CREATE SCHEMA IF NOT EXISTS public;";
        stm.execute(sql);

        Reader r;

        String sqlFile = "..";
                //"../../src/sql/create-tables.sql";
        System.out.println("----" + getClass().getClassLoader().getResource(sqlFile));
        r = new InputStreamReader(
                //getClass().getClassLoader().getResourceAsStream("../src/sql/create-tables.sql")
                getClass().getClassLoader().getResourceAsStream(sqlFile)
        );

        //r = new FileReader("src/sql/create-tables.sql");
        DbUtils.executeSql(con, r);
        r.close();
    }

    @After
    public void tearDown()
            throws SQLException
    {
        Statement stm = con.createStatement();
        String sql = "DROP SCHEMA IF EXISTS public CASCADE ;";
        stm.execute(sql);
    }

    /**
     * Test of addGroup method, of class PropertiesStorage.
     */
    @Test
    public void testAddGroup() throws Exception {
        System.out.println("--- addGroup ---");
        PropertiesStorage instance = new PropertiesStorage(con,
                "properties", "propertyGroups");
        instance.addGroup("newGroup");
    }

    /**
     * Test of getGroup method, of class PropertiesStorage.
     */
    @Test
    public void testGetGroup() throws Exception {
        System.out.println("--- getGroup ---");
        PropertiesStorage instance = new PropertiesStorage(con,
                "properties", "propertyGroups");

        String groupName = "newGroup";
        instance.addGroup(groupName);
        PropertyGroup grp = instance.getGroup(groupName);
        assertNotNull(grp);
    }
}
