package org.server.core;

import org.db.flyway.tables.pojos.Participants;
import org.db.flyway.tables.records.ParticipantsRecord;
import org.jooq.Configuration;
import org.jooq.Table;
import org.jooq.impl.DAOImpl;

class BaseDao extends DAOImpl<ParticipantsRecord, Participants, Integer> {

    protected BaseDao(Table table, Class type, Configuration conf) {
        super(table, type, conf);
    }

    @Override
    public Integer getId(Participants participants) {
        return participants.getId();
    }
}
