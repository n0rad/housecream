/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.api.domain;

public class Order {

    public enum OrderType {
        ASC, DESC;

        public static OrderType valueOfUncaseSensitive(String str) {
            String upperCase = str.toUpperCase();
            for (OrderType element : values()) {
                if (upperCase.equals(element.name())) {
                    return element;
                }
            }
            return null;
        }
    }

    private String property;
    private boolean ascending;

    public Order() {
    }

    /**
     * @param propertyAndOrder
     *            ex : "status asc"
     */
    public Order(String propertyAndOrder) {
        //        Preconditions.checkNotNull(propertyAndOrder, "propertyAndOrder is a mandatory parameter");
        //        String[] split = propertyAndOrder.split(" ");
        //        Preconditions.checkState(split.length == 2, "propertyAndOrder must be of format : 'property asc'");
        //        OrderType type = OrderType.valueOfUncaseSensitive(split[1]);
        //        Preconditions.checkNotNull(propertyAndOrder, "orderType '%s' does not exists", split[1]);
        //        property = split[0];
        //        this.ascending = type == OrderType.ASC;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
