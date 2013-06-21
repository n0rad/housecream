package net.awired.housecream.server.application;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.HConsistencyLevel;

public class ConsistencyLevelPolicy extends ConfigurableConsistencyLevel {

    public ConsistencyLevelPolicy() {
        setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
        setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);

        setConsistencyLevelForCfOperation(HConsistencyLevel.ONE, "elements", OperationType.READ);
    }
}
