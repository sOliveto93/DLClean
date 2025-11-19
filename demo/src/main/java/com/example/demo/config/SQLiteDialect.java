package com.example.demo.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

        public SQLiteDialect() {
            registerColumnType(Types.INTEGER, "integer");
            registerColumnType(Types.VARCHAR, "text");
            registerColumnType(Types.FLOAT, "real");
            registerColumnType(Types.DOUBLE, "real");
            registerColumnType(Types.BOOLEAN, "integer");
            registerColumnType(Types.BIGINT, "integer");
        }
    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    private static class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
        @Override
        public boolean supportsIdentityColumns() {
            return true;
        }

        @Override
        public String getIdentitySelectString(String table, String column, int type) {
            return "select last_insert_rowid()";
        }

        @Override
        public String getIdentityColumnString(int type) {
            return "integer";
        }
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    }


