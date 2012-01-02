package net.awired.housecream.server.storage.dao;

import javax.persistence.NoResultException;

public class DoesNotExistsException extends Exception {

    public DoesNotExistsException(String string, NoResultException e) {
        // TODO Auto-generated constructor stub
    }

    public DoesNotExistsException(String string) {
        // TODO Auto-generated constructor stub
    }

}
