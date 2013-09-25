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
package org.housecream.server.storage.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.rule.Condition;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class RuleDao {

    private final Session session;
    private final PreparedStatement deleteStatement;
    private final PreparedStatement insertStatement;
    private final PreparedStatement selectStatement;
    private final PreparedStatement allStatement;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public RuleDao(Session session) {
        this.session = session;
        deleteStatement = session.prepare("DELETE FROM rules where id = ?");
        insertStatement = session
                .prepare("INSERT INTO rules(id, name, salience, conditions, consequences) VALUES(?, ?, ?, ?, ?)");
        selectStatement = session.prepare("SELECT * FROM rules WHERE id = ?");
        allStatement = session.prepare("SELECT * FROM rules");
    }

    public void delete(UUID ruleId) {
        session.execute(deleteStatement.bind(ruleId));
    }

    public void save(Rule rule) {
        if (rule.getId() == null) {
            rule.setId(UUID.randomUUID());
        }
        session.execute(insertStatement.bind(rule.getId(),//
                rule.getName(), //
                rule.getSalience(), //
                toJson(rule.getConditions()), //
                toJson(rule.getConsequences())));
    }

    public Rule find(UUID ruleId) {
        return map(session.execute(selectStatement.bind(ruleId)).one());
    }

    public List<Rule> findAll() {
        ResultSet res = session.execute(allStatement.bind());
        List<Rule> rules = new ArrayList<>();
        for (Row row : res) {
            rules.add(map(row));
        }
        return rules;

    }

    public void deleteAll() {
        session.execute("TRUNCATE rules");
    }

    private List<String> toJson(List<?> obj) {
        List<String> res = new ArrayList<>(obj.size());
        for (Object object : obj) {
            try {
                res.add(mapper.writeValueAsString(object));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Cannot serialize rule element : " + object);
            }
        }
        return res;
    }

    private <T> List<T> fromJson(List<String> jsons, Class<T> valueType) {
        List<T> res = new ArrayList<>(jsons.size());
        for (String element : jsons) {
            try {
                res.add(mapper.readValue(element, valueType));
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read rule element" + element);
            }
        }
        return res;
    }

    private Rule map(Row row) {
        Rule rule = new Rule();
        rule.setId(row.getUUID("id"));
        rule.setName(row.getString("name"));
        rule.setConditions(fromJson(row.getList("conditions", String.class), Condition.class));
        rule.setConsequences(fromJson(row.getList("consequences", String.class), Consequence.class));
        rule.setSalience(row.getInt("salience"));
        return rule;
    }
}
