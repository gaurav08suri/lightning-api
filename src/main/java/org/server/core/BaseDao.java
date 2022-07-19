package org.server.core;

import org.db.flyway.tables.pojos.Participants;
import org.db.flyway.tables.pojos.Runner;
import org.db.flyway.tables.records.ParticipantsRecord;
import org.db.flyway.tables.records.RunnerRecord;
import org.jooq.Configuration;
import org.jooq.Table;
import org.jooq.impl.DAOImpl;

public class BaseDao extends DAOImpl<ParticipantsRecord, Participants, Integer> {

    public BaseDao(Table table, Class type, Configuration conf) {
        super(table, type, conf);
    }

    @Override
    public Integer getId(Participants participants) {
        return participants.getId();
    }
}

class BaseRDao extends DAOImpl<RunnerRecord, Runner, Integer> {

    public BaseRDao(Table table, Class type, Configuration conf) {
        super(table, type, conf);
    }

    @Override
    public Integer getId(Runner participants) {
        return participants.getId();
    }
}