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
public class PropertiesStorage {
    Connection con;
    String propsTable;
    String propGroupsTable;

    public Connection getConnection() {
        return con;
    }

    protected PropertiesStorage() {
    }

    protected void setConnection(Connection con) {
        this.con = con;
    }

    protected void setPropsTable(String propsTable) {
        this.propsTable = propsTable;
    }

    protected void setPropGroupsTable(String propGroupsTable) {
        this.propGroupsTable = propGroupsTable;
    }

    public PropertiesStorage(Connection con, String propsTable, String propGroupsTable) {
        this.con = con;
        this.propsTable = propsTable;
        this.propGroupsTable = propGroupsTable;
    }

    String getPropsTable() {
        return propsTable;
    }

    String getPropGroupsTable() {
        return propGroupsTable;
    }

    public void addGroup(String name)
            throws SQLException
    {
        Connection con = getConnection();
        if (con != null) {
            Statement stmt = con.createStatement();
            String sql = String.format("INSERT INTO %s(name) VALUES('%s');",
                    propGroupsTable, name);
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();
        }
    }

    public PropertyGroup getGroup(String name)
            throws SQLException
    {
        PropertyGroup grp = null;

        Connection con = getConnection();
        if (con != null) {
            Statement stmp = con.createStatement();
            String sql = String.format("SELECT id FROM %s WHERE name = '%s'",
                    propGroupsTable, name);
            System.out.println(sql);
            ResultSet result = stmp.executeQuery(sql);
            if (result.next()) {
                String id = result.getString("id");

                grp = new PropertyGroup(this, id);
            }
            result.close();
        }

        return grp;
    }

    public PropertyGroup getGroup(String name, boolean createIfNotExists)
            throws SQLException
    {
        PropertyGroup grp = getGroup(name);

        if (grp == null) {
            addGroup(name);
            grp = getGroup(name);
        }

        return grp;
    }
}
