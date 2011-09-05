/**
 * $Id:$
 *
 * This file is part of Business Intranet Collaboratif (BIC).
 *
 * Labo NTIC Production 2008
 * 
 * @author Administrateur
 * @creation 4 sept. 2008
 * @version 1.0
 */
package net.awired.acse.kernel.implementation.exception;

public class AcseException extends Exception {

    private static final long serialVersionUID = 8636883060192348830L;

    private String[]          vars;

    public String[] getVars() {
        return this.vars;
    }

    public AcseException setVars(String... vars) {
        this.vars = vars;
        return this;
    }

    public AcseException() {
        super();
    }

    // public AcseException(Throwable cause) {
    // super(cause);
    // }
    //
    // public AcseException(String message, String... vars) {
    // super(message);
    // this.vars = vars;
    // }
    //
    // public AcseException(String message, Exception cause, String... vars) {
    // super(message, cause);
    // this.vars = vars;
    // }
}
