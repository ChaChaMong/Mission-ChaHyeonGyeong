package com.ll.domain;

import com.ll.simpleDb.SimpleDb;
import com.ll.simpleDb.Sql;
import standard.util.AppConfig;

import java.util.List;

public class QuotationDao {
    private static final String HOST = AppConfig.getProperty("db.host");
    private static final String ID = AppConfig.getProperty("db.id");
    private static final String PW = AppConfig.getProperty("db.password");
    private static final String DBNAME = AppConfig.getProperty("db.dbName");
    SimpleDb simpleDb = new SimpleDb(HOST, ID, PW, DBNAME);

    public List<Quotation> selectAllQuotations() {
        Sql sql = simpleDb.genSql();
        /*
        == rawSql ==
        SELECT *
        FROM quotation
        ORDER BY id ASC
        */
        sql.append("SELECT * FROM quotation ORDER BY id ASC");

        return sql.selectRows(Quotation.class);
    }

    public long selectCountQuotationsById(long id) {
        Sql sql = simpleDb.genSql();
        /*
        == rawSql ==
        SELECT COUNT(*)
        FROM quotation
        WHERE id = 1;
        */
        sql.append("SELECT COUNT(*) FROM quotation WHERE id = ?", id);

        return sql.selectLong();
    }

    public Quotation selectQuotationsById(long id) {
        Sql sql = simpleDb.genSql();
        /*
        == rawSql ==
        SELECT *
        FROM quotation
        WHERE id = 1;
        */
        sql.append("SELECT * FROM quotation WHERE id = ?", id);

        return sql.selectRow(Quotation.class);
    }

    public long insertQuotation(Quotation quotation) {
        Sql sql = simpleDb.genSql();
        /*
        == rawSql ==
        INSERT INTO quotation
        SET createdDate = NOW() ,
        modifiedDate = NOW() ,
        content = '내용' ,
        authorName = '작가'
        */
        sql.append("INSERT INTO quotation")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", content = ?", quotation.getContent())
                .append(", authorName = ?", quotation.getAuthorName());

        return sql.insert(); // AUTO_INCREMENT 에 의해서 생성된 주키 리턴
    }

    public long deleteQuotationById(long id) {
        Sql sql = simpleDb.genSql();

        /*
        == rawSql ==
        DELETE FROM quotation
        WHERE id = '1'
        */
        sql.append("DELETE")
                .append("FROM quotation")
                .append("WHERE id = ?", id);

        // 삭제된 row 개수
        return sql.delete();
    }

    public long updateQuotationById(Quotation quotation) {
        Sql sql = simpleDb.genSql();

        /*
        == rawSql ==
        UPDATE quotation
        SET content = '내용'
        , authorName = '작가'
        , modifiedDate = NOW()
        WHERE id = '1'
        */
        sql
                .append("UPDATE quotation")
                .append("SET content = ?", quotation.getContent())
                .append(", authorName = ?", quotation.getAuthorName())
                .append(", modifiedDate = NOW()")
                .append("WHERE id = ?", quotation.getId());

        // 수정된 row 개수
        return sql.update();
    }
}
