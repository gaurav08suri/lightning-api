/*
 * This file is generated by jOOQ.
 */
package org.db.flyway.tables;


import java.util.Arrays;
import java.util.List;

import org.db.flyway.Keys;
import org.db.flyway.RegistrationApp;
import org.db.flyway.tables.records.ParticipantsRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row15;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Participants extends TableImpl<ParticipantsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>registration_app.participants</code>
     */
    public static final Participants PARTICIPANTS = new Participants();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ParticipantsRecord> getRecordType() {
        return ParticipantsRecord.class;
    }

    /**
     * The column <code>registration_app.participants.id</code>.
     */
    public final TableField<ParticipantsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>registration_app.participants.first_name</code>.
     */
    public final TableField<ParticipantsRecord, String> FIRST_NAME = createField(DSL.name("first_name"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants.last_name</code>.
     */
    public final TableField<ParticipantsRecord, String> LAST_NAME = createField(DSL.name("last_name"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants.age</code>.
     */
    public final TableField<ParticipantsRecord, Integer> AGE = createField(DSL.name("age"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>registration_app.participants.mobile</code>.
     */
    public final TableField<ParticipantsRecord, String> MOBILE = createField(DSL.name("mobile"), SQLDataType.VARCHAR(15), this, "");

    /**
     * The column <code>registration_app.participants.mail</code>.
     */
    public final TableField<ParticipantsRecord, String> MAIL = createField(DSL.name("mail"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>registration_app.participants.category</code>.
     */
    public final TableField<ParticipantsRecord, String> CATEGORY = createField(DSL.name("category"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.country</code>.
     */
    public final TableField<ParticipantsRecord, String> COUNTRY = createField(DSL.name("country"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.language</code>.
     */
    public final TableField<ParticipantsRecord, String> LANGUAGE = createField(DSL.name("language"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.center</code>.
     */
    public final TableField<ParticipantsRecord, String> CENTER = createField(DSL.name("center"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.rollno</code>.
     */
    public final TableField<ParticipantsRecord, String> ROLLNO = createField(DSL.name("rollno"), SQLDataType.VARCHAR(20), this, "");

    /**
     * The column <code>registration_app.participants.level1_score</code>.
     */
    public final TableField<ParticipantsRecord, String> LEVEL1_SCORE = createField(DSL.name("level1_score"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.level2_score</code>.
     */
    public final TableField<ParticipantsRecord, String> LEVEL2_SCORE = createField(DSL.name("level2_score"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.level3_score</code>.
     */
    public final TableField<ParticipantsRecord, String> LEVEL3_SCORE = createField(DSL.name("level3_score"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>registration_app.participants.level4_score</code>.
     */
    public final TableField<ParticipantsRecord, String> LEVEL4_SCORE = createField(DSL.name("level4_score"), SQLDataType.VARCHAR(100), this, "");

    private Participants(Name alias, Table<ParticipantsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Participants(Name alias, Table<ParticipantsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>registration_app.participants</code> table reference
     */
    public Participants(String alias) {
        this(DSL.name(alias), PARTICIPANTS);
    }

    /**
     * Create an aliased <code>registration_app.participants</code> table reference
     */
    public Participants(Name alias) {
        this(alias, PARTICIPANTS);
    }

    /**
     * Create a <code>registration_app.participants</code> table reference
     */
    public Participants() {
        this(DSL.name("participants"), null);
    }

    public <O extends Record> Participants(Table<O> child, ForeignKey<O, ParticipantsRecord> key) {
        super(child, key, PARTICIPANTS);
    }

    @Override
    public Schema getSchema() {
        return RegistrationApp.REGISTRATION_APP;
    }

    @Override
    public Identity<ParticipantsRecord, Integer> getIdentity() {
        return (Identity<ParticipantsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ParticipantsRecord> getPrimaryKey() {
        return Keys.PK_PARTICIPANT;
    }

    @Override
    public List<UniqueKey<ParticipantsRecord>> getKeys() {
        return Arrays.<UniqueKey<ParticipantsRecord>>asList(Keys.PK_PARTICIPANT, Keys.MOBILE_UNIQ, Keys.MAIL_UNIQ);
    }

    @Override
    public Participants as(String alias) {
        return new Participants(DSL.name(alias), this);
    }

    @Override
    public Participants as(Name alias) {
        return new Participants(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Participants rename(String name) {
        return new Participants(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Participants rename(Name name) {
        return new Participants(name, null);
    }

    // -------------------------------------------------------------------------
    // Row15 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row15<Integer, String, String, Integer, String, String, String, String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row15) super.fieldsRow();
    }
}
