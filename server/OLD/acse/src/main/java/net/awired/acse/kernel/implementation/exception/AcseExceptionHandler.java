/**
 * $Id:$
 *
 * This file is part of Business Intranet Collaboratif (BIC).
 *
 * Labo NTIC Production 2008
 * 
 * @author alemaire
 * @creation 26 sept. 2008
 * @version 1.0
 */
package net.awired.acse.kernel.implementation.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class AcseExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("custom error show::::::");
        e.printStackTrace();
    }

}
