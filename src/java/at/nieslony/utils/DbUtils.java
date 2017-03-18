/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.nieslony.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author claas
 */
public class DbUtils {
    public static void executeSql(Connection con, Reader reader)
            throws SQLException, IOException
    {
        Logger logger = Logger.getLogger(java.util.logging.ConsoleHandler.class.toString());

        Statement stm = con.createStatement();
        BufferedReader br = new BufferedReader(reader);
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            int pos = line.indexOf("--");
            if (pos != -1)
                line = line.substring(0, pos);
            sb.append(line);
        }

        String oneLiner = sb.toString();
        oneLiner = oneLiner.replaceAll("/\\*.*?\\*/", "");

        for (String s : oneLiner.split(";;")) {
            //s = s + ";";
            logger.info(String.format("Add sql to batch: %s", s));
            stm.addBatch(s);
        }
        int[] results;
        try {
            results = stm.executeBatch();
        }
        catch (SQLException ex) {
            throw ex.getNextException();
        }
        finally {
            stm.close();
        }
        if (results == null) {
            logger.severe("Cannot get bresults from SQL batch");
        }
        else {
            for (int res : results) {
                logger.info(String.format("Result: %d", res));
            }
        }
    }
}
