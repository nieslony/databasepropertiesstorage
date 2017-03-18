/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.nieslony.databasepropertiesstorage;

import static at.nieslony.databasepropertiesstorage.PropertiesStorageTest.con;
import at.nieslony.utils.DbUtils;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author claas
 */
public class PropertyGroupTest {
    static PropertiesStorage ps;
    static PropertyGroup pg;

    public PropertyGroupTest() {
    }

    @BeforeClass
    public static void setUpClass()
            throws ClassNotFoundException, SQLException, IOException
    {
        System.out.println("Opening database...");
        Class.forName("org.postgresql.Driver");
        con = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/spielwiese",
                        "tester", "tester");
        con.setAutoCommit(true);
        System.out.println("... success.");

        String sql;
        Statement stm = con.createStatement();

        sql = "DROP SCHEMA IF EXISTS public CASCADE ;";
        stm.execute(sql);
        sql = "CREATE SCHEMA IF NOT EXISTS public;";
        stm.execute(sql);

        FileReader fr = new FileReader("src/sql/create-tables.sql");
        DbUtils.executeSql(con, fr);
        fr.close();

        ps = new PropertiesStorage(con, "properties", "propertyGroups");
        ps.addGroup("testGroup");
        pg = ps.getGroup("testGroup");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProperty method, of class PropertyGroup.
     */
    @Test
    public void testGetProperty() throws Exception {
        System.out.println("--- getProperty ---");

        String value = "TestValue";

        pg.setProperty("testName", value);
        String v = pg.getProperty("testName");

        org.junit.Assert.assertEquals(value, v);
    }

    /**
     * Test of setProperty method, of class PropertyGroup.
     */
    @Test
    public void testSetProperty() throws Exception {
        System.out.println("--- setProperty ---");
        pg.setProperty("testName", "testValue");
    }

}
