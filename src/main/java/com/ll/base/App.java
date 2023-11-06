package com.ll.base;

import com.ll.domain.QuotationController;

import java.util.Scanner;

public class App {
    private Scanner scanner;

    public App() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        QuotationController quotationController = new QuotationController(scanner);

        while (true) {
            System.out.print("명령) ");

            String cmd = scanner.nextLine();

            switch (cmd) {
                case "등록":
                    quotationController.actionWrite();
                    break;
                case "종료":
                    scanner.close();
                    return;

            }
        }
    }
}