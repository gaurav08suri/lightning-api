/*
 * This file is generated by jOOQ.
 */
package org.db.flyway.tables;


import java.util.Arrays;
import java.util.List;

import org.db.flyway.Keys;
import org.db.flyway.RegistrationApp;
import org.db.flyway.tables.records.ParticipantsLevelsRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ParticipantsLevels extends TableImpl<ParticipantsLevelsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>registration_app.participants_levels</code>
     */
    public static final ParticipantsLevels PARTICIPANTS_LEVELS = new ParticipantsLevels();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ParticipantsLevelsRecord> getRecordType() {
        return ParticipantsLevelsRecord.class;
    }

    /**
     * The column <code>registration_app.participants_levels.participant_id</code>.
     */
    public final TableField<ParticipantsLevelsRecord, Integer> PARTICIPANT_ID = createField(DSL.name("participant_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>registration_app.participants_levels.level_id</code>.
     */
    public final TableField<ParticipantsLevelsRecord, Integer> LEVEL_ID = createField(DSL.name("level_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>registration_app.participants_levels.score</code>.
     */
    public final TableField<ParticipantsLevelsRecord, Integer> SCORE = createField(DSL.name("score"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>registration_app.participants_levels.rank</code>.
     */
    public final TableField<ParticipantsLevelsRecord, Integer> RANK = createField(DSL.name("rank"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>registration_app.participants_levels.result</code>.
     */
    public final TableField<ParticipantsLevelsRecord, String> RESULT = createField(DSL.name("result"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants_levels.custom_1</code>.
     */
    public final TableField<ParticipantsLevelsRecord, String> CUSTOM_1 = createField(DSL.name("custom_1"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants_levels.custom_2</code>.
     */
    public final TableField<ParticipantsLevelsRecord, String> CUSTOM_2 = createField(DSL.name("custom_2"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants_levels.custom_3</code>.
     */
    public final TableField<ParticipantsLevelsRecord, String> CUSTOM_3 = createField(DSL.name("custom_3"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants_levels.custom_4</code>.
     */
    public final TableField<ParticipantsLevelsRecord, String> CUSTOM_4 = createField(DSL.name("custom_4"), SQLDataType.VARCHAR(50), this, "");

    private ParticipantsLevels(Name alias, Table<ParticipantsLevelsRecord> aliased) {
        this(alias, aliased, null);
    }

    private ParticipantsLevels(Name alias, Table<ParticipantsLevelsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>registration_app.participants_levels</code> table reference
     */
    public ParticipantsLevels(String alias) {
        this(DSL.name(alias), PARTICIPANTS_LEVELS);
    }

    /**
     * Create an aliased <code>registration_app.participants_levels</code> table reference
     */
    public ParticipantsLevels(Name alias) {
        this(alias, PARTICIPANTS_LEVELS);
    }

    /**
     * Create a <code>registration_app.participants_levels</code> table reference
     */
    public ParticipantsLevels() {
        this(DSL.name("participants_levels"), null);
    }

    public <O extends Record> ParticipantsLevels(Table<O> child, ForeignKey<O, ParticipantsLevelsRecord> key) {
        super(child, key, PARTICIPANTS_LEVELS);
    }

    @Override
    public Schema getSchema() {
        return RegistrationApp.REGISTRATION_APP;
    }

    @Override
    public List<ForeignKey<ParticipantsLevelsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ParticipantsLevelsRecord, ?>>asList(Keys.PARTICIPANTS_LEVELS__FK_PARTICIPANT, Keys.PARTICIPANTS_LEVELS__FK_LEVEL);
    }

    private transient Participants _participants;
    private transient Levels _levels;

    public Participants participants() {
        if (_participants == null)
            _participants = new Participants(this, Keys.PARTICIPANTS_LEVELS__FK_PARTICIPANT);

        return _participants;
    }

    public Levels levels() {
        if (_levels == null)
            _levels = new Levels(this, Keys.PARTICIPANTS_LEVELS__FK_LEVEL);

        return _levels;
    }

    @Override
    public ParticipantsLevels as(String alias) {
        return new ParticipantsLevels(DSL.name(alias), this);
    }

    @Override
    public ParticipantsLevels as(Name alias) {
        return new ParticipantsLevels(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ParticipantsLevels rename(String name) {
        return new ParticipantsLevels(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ParticipantsLevels rename(Name name) {
        return new ParticipantsLevels(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, Integer, Integer, Integer, String, String, String, String, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
