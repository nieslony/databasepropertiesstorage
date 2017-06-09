package at.nieslony.databasepropertiesstorage;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

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

    class CachedString {
        long lastUpdate;
        String value;
    }

    private final HashMap<String, CachedString> cachedValues = new HashMap<>();

    public PropertyGroup(PropertiesStorage ps,String id) {
        this.id = id;
        this.ps = ps;
    }

    public String getProperty(String name)
            throws SQLException
    {
        String ret = null;

        if (ps.getCacheTimeout() > 0 && cachedValues.containsKey(name)) {
            long now = System.currentTimeMillis();
            CachedString cachedValue = cachedValues.get(name);
            if (cachedValue.lastUpdate + ps.getCacheTimeout() > now)
                return cachedValue.value;
        }

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

        if (ps.getCacheTimeout() > 0) {
            setCachedValue(name, ret);
        }

        return ret;
    }

    private void setCachedValue(String key, String value) {
        CachedString cachedValue = cachedValues.get(key);
        if (cachedValue == null) {
            cachedValue = new CachedString();
            cachedValues.put(key, cachedValue);
        }
        cachedValue.lastUpdate = System.currentTimeMillis();
        cachedValue.value = value;
    }

    public String getProperty(String name, String default_value)
            throws SQLException
    {
        String ret = getProperty(name);

        if (ret != null)
            return ret;

        ret = default_value;
        if (ps.getCacheTimeout() > 0) {
            setCachedValue(name, ret);
        }

        return ret;
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

        if (ps.getCacheTimeout() > 0) {
            setCachedValue(name, value);
        }
    }
}
