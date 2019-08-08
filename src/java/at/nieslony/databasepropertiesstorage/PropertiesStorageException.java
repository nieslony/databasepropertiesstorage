/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.nieslony.databasepropertiesstorage;

/**
 *
 * @author claas
 */
public class PropertiesStorageException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7514936251176991681L;

	/**
     * Creates a new instance of <code>PropertiesStorageException</code> without
     * detail message.
     */
    private PropertiesStorageException() {
    }

    /**
     * Constructs an instance of <code>PropertiesStorageException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PropertiesStorageException(String msg) {
        super(msg);
    }
}
