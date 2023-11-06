package com.ll.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuotationController {
    private Scanner scanner;
    private List<Quotation> quotations;

    public QuotationController(Scanner scanner) {
        this.scanner = scanner;
        quotations = new ArrayList<>();
    }
    public void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        Quotation quotation = new Quotation(quotations.size() + 1, content);
        quotations.add(quotation);

        System.out.printf("%d번 명언이 등록되었습니다.\n", quotation.getId());
    }
}
