package at.nieslony.databasepropertiesstorage;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author claas
 */
public class PropertyGroup {
    private PropertyGroup() {}

    String id;
    PropertiesStorage ps;

    public PropertyGroup(PropertiesStorage ps,String id) {
        this.id = id;
        this.ps = ps;
    }

    public String getProperty(String name)
            throws SQLException
    {
        String ret = null;

        Connection con = ps.getConnection();
        Statement stm = con.createStatement();
        String sql = String.format(
                "SELECT value FROM %s WHERE group_id='%s' AND name='%s';",
                ps.getPropsTable(),
                id,
                name);
        System.out.println(sql);
        ResultSet result = stm.executeQuery(sql);
        if (result.next()) {
            ret = result.getString("value");
        }
        result.close();
        stm.close();

        return ret;
    }

    public String getProperty(String name, String default_value)
            throws SQLException
    {
        String ret = getProperty(name);
        return ret == null ? default_value : ret;
    }

    public void setProperty(String name, String value)
            throws SQLException
    {
        Connection con = ps.getConnection();
        Statement stm = con.createStatement();
        String sql = String.format(
                "SELECT set_property('%s', '%s', '%s')",
                id,
                name,
                value);
        System.out.println(sql);
        stm.execute(sql);
        stm.close();
    }
}
