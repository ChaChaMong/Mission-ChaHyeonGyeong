package com.ll.base;

import com.ll.domain.QuotationController;

import java.util.Scanner;

public class App {
    private final Scanner scanner;

    public App() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        QuotationController quotationController = new QuotationController(scanner);

        while (true) {
            System.out.print("명령) ");

            String cmd = scanner.nextLine();

            try {
                Rq rq = new Rq(cmd);

                switch (rq.getAction()) {
                    case "등록":
                        quotationController.actionWrite();
                        break;
                    case "목록":
                        quotationController.actionList();
                        break;
                    case "삭제":
                        quotationController.actionRemove(rq);
                        break;
                    case "수정":
                        quotationController.actionModify(rq);
                        break;
                    case "빌드":
                        quotationController.actionBuild();
                        break;
                    case "종료":
                        scanner.close();
                        return;
                    default:
                        System.out.println("등록된 명령어가 아닙니다.");
                        
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("올바른 명령 형식으로 입력해 주세요.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
