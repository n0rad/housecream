/**
 * $Id:$
 *
 * This file is part of Business Intranet Collaboratif (BIC).
 *
 * Labo NTIC Production 2008
 * 
 * @author n0rad
 * @creation 12 oct. 2008
 * @version 1.0
 */
package net.awired.acse.kernel;

import net.awired.ajsl.Callback;

public class Kernel {
    public static void main(String[] vars) {
        new Callback(new Kernel(), "", new Kernel());
        System.out.println("loading acse Kernel");
    }
}
