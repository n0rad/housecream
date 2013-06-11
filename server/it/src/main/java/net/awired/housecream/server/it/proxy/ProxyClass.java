/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.it.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class ProxyClass<T> implements InvocationHandler {
    T obj;

    abstract protected void handleBefore(Method m, Object[] args);

    abstract protected void handleAfter(Method m, Object[] args);

    abstract protected void handleSuccess(Method m, Object[] args, Object result);

    public static <T> T BuildProxy(ProxyClass<T> proxyClass, Class<T> resultClass) {
        return (T) Proxy.newProxyInstance(resultClass.getClassLoader(), new Class[] { resultClass }, proxyClass);
    }

    public ProxyClass(T o) {
        obj = o;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result = null;
        try {
            handleBefore(m, args);
            result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } finally {
            handleAfter(m, args);
        }
        handleSuccess(m, args, result);
        return result;
    }
}
