package com.ll.domain;

import com.ll.base.Rq;
import standard.util.JsonFileIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class QuotationController {
    private final Scanner scanner;
    private final List<Quotation> quotations;
    private int lastQuotationId;

    private final JsonFileIO<Quotation> jsonFileIO;
    String jsonLiveFilePath = "src/main/resources/liveData.json"; // 실시간 JSON 파일의 경로를 설정

    public QuotationController(Scanner scanner) {
        this.scanner = scanner;
        jsonFileIO = new JsonFileIO<>();
        List<Quotation> tempQuotations = jsonFileIO.readFile(jsonLiveFilePath, Quotation.class);
        quotations = Objects.requireNonNullElseGet(tempQuotations, ArrayList::new);
        lastQuotationId = getLastQuotationId();
    }
    public void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.print("작가 : ");
        String authorName = scanner.nextLine();

        lastQuotationId++;
        int id = lastQuotationId;

        Quotation quotation = new Quotation(id, content, authorName);
        quotations.add(quotation);
        jsonFileIO.writeFile(quotations, jsonLiveFilePath);
        System.out.printf("%d번 명언이 등록되었습니다.\n", quotation.getId());
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (int i = quotations.size() - 1; i >= 0; i--) {
            Quotation quotation = quotations.get(i);
            System.out.printf("%d / %s / %s\n", quotation.getId(), quotation.getAuthorName(), quotation.getContent());
        }
    }

    public void actionRemove(Rq rq) {
        int id = rq.getParamAsInt("id", 0);

        if (id == 0) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        int index = findQuotationIndexById(id);

        if (index == -1) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        quotations.remove(index);
        jsonFileIO.writeFile(quotations, jsonLiveFilePath);

        System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
    }

    public void actionModify(Rq rq) {
        int id = rq.getParamAsInt("id", 0);

        if (id == 0) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        int index = findQuotationIndexById(id);

        if (index == -1) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", quotations.get(index).getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.printf("작가(기존) : %s\n", quotations.get(index).getAuthorName());
        System.out.print("작가 : ");
        String authorName = scanner.nextLine();

        quotations.get(index).setContent(content);
        quotations.get(index).setAuthorName(authorName);
        jsonFileIO.writeFile(quotations, jsonLiveFilePath);

        System.out.printf("%d번 명언이 수정되었습니다.\n", id);
    }

    private int findQuotationIndexById(int id) {
        for (int i = 0; i < quotations.size(); i++) {
            Quotation quotation = quotations.get(i);
            if (quotation.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int getLastQuotationId() {
        int lastId = 0;
        for (Quotation quotation : quotations) {
            if (quotation.getId() > lastId) {
                lastId = quotation.getId();
            }
        }

        return lastId;
    }
}
