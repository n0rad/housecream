/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package net.awired.housecream.server.api.domain;

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
