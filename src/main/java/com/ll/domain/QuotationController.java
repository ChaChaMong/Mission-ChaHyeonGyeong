package com.ll.domain;

import com.ll.base.Rq;
import com.ll.simpleDb.SimpleDb;
import standard.util.AppConfig;
import standard.util.JsonFileIO;

import java.util.List;
import java.util.Scanner;

public class QuotationController {
    private final Scanner scanner;
    private final SimpleDb simpleDb;
    private final QuotationDao quotationDao;

    private final JsonFileIO<Quotation> jsonFileIO;
    private static final String JSON_BUILD_FILE_PATH = "src/main/resources/data.json"; // 빌드 JSON 파일의 경로를 설정

    private static final String HOST = AppConfig.getProperty("db.host");
    private static final String ID = AppConfig.getProperty("db.id");
    private static final String PW = AppConfig.getProperty("db.password");
    private static final String DBNAME = AppConfig.getProperty("db.dbName");

    public QuotationController(Scanner scanner) {
        this.scanner = scanner;
        quotationDao = new QuotationDao();
        simpleDb = new SimpleDb(HOST, ID, PW, DBNAME);
        jsonFileIO = new JsonFileIO<>();
    }
    public void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.print("작가 : ");
        String authorName = scanner.nextLine();

        Quotation quotation = new Quotation(content, authorName);
        long id = quotationDao.insertQuotation(simpleDb, quotation);

        System.out.printf("%d번 명언이 등록되었습니다.\n", id);
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<Quotation> quotations;
        quotations = quotationDao.selectAllQuotations(simpleDb);
        for (int i = quotations.size() - 1; i >= 0; i--) {
            Quotation quotation = quotations.get(i);
            System.out.printf("%d / %s / %s\n", quotation.getId(), quotation.getAuthorName(), quotation.getContent());
        }
    }

    public void actionRemove(Rq rq) {
        long id = rq.getParamAsLong("id", 0);

        if (id == 0) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        long index = quotationDao.deleteQuotationById(simpleDb, id);

        if (index == 0) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
        } else {
            System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
        }
    }

    public void actionModify(Rq rq) {
        long id = rq.getParamAsLong("id", 0);

        if (id == 0) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        Quotation quotation = quotationDao.selectQuotationsById(simpleDb, id);

        if (quotation == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", quotation.getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.printf("작가(기존) : %s\n", quotation.getAuthorName());
        System.out.print("작가 : ");
        String authorName = scanner.nextLine();

        quotation.setContent(content);
        quotation.setAuthorName(authorName);

        quotationDao.updateQuotationById(simpleDb, quotation);

        System.out.printf("%d번 명언이 수정되었습니다.\n", id);
    }

    public void actionBuild() {
        List<Quotation> quotations = quotationDao.selectAllQuotations(simpleDb);
        jsonFileIO.writeFile(quotations, JSON_BUILD_FILE_PATH);

        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
