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
package org.housecream.server.api.resource;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.housecream.server.api.domain.Order;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.api.resource.Security.Scopes;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.core.lang.exception.NotFoundException;

@Path("/rules")
@Security(Scopes.RULE_READ)
public interface RulesResource {

    @GET
    List<Rule> getRules(@QueryParam("length") Integer length, //
                        @QueryParam("start") UUID start, //
                        @QueryParam("search") String search, //
                        @QueryParam("searchProperty") List<String> searchProperties, //
                        @QueryParam("order") List<Order> orders);

    @DELETE
    @Security(Scopes.RULE_WRITE)
    void deleteAllRules();

    @PUT
    @Security(Scopes.RULE_WRITE)
    Rule createRule(@Valid Rule rule);

    @GET
    @Path("/validator")
    ClientValidatorInfo getRuleValidator();

    @Path("/{id}")
    RuleResource rule(@PathParam("id") UUID ruleId);

    interface RuleResource {

        @DELETE
        @Security(Scopes.RULE_WRITE)
        void deleteRule(@PathParam("id") UUID ruleId);

        @GET
        Rule getRule(@PathParam("id") UUID ruleId) throws NotFoundException;

    }

}
