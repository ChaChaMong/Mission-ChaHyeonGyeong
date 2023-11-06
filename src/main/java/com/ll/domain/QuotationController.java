package com.ll.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuotationController {
    private Scanner scanner;
    private List<Quotation> quotations;
    private int lastQuotationId;

    public QuotationController(Scanner scanner) {
        this.scanner = scanner;
        quotations = new ArrayList<>();
        lastQuotationId = 0;
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
}
