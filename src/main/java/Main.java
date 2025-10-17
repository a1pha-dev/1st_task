import cli.AddStringProcess;
import cli.ConnectOrCreateTableProcess;
import cli.ExportTableProcess;
import cli.PrintTableProcess;
import db.RepositoryImpl;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RepositoryImpl repository = RepositoryImpl.getInstance();
        repository.setTableName(null);

        while (true) {
            System.out.print("""
                    Выберите действие:
                        1. Показать все таблицы
                        2. Подключиться или создать таблицу
                        3. Развернуть строку
                        4. Конкатенировать строки
                        5. Экспортировать в Excel
                        0. Выйти
                    """);

            switch (scanner.nextLine().trim()) {
                case "1" -> PrintTableProcess.run(repository);
                case "2" -> ConnectOrCreateTableProcess.run(scanner, repository);
                case "3" -> AddStringProcess.runReverse(scanner, repository);
                case "4" -> AddStringProcess.runConcatenate(scanner, repository);
                case "5" -> ExportTableProcess.run(repository);
                case "0" -> {
                    System.out.println("Выход.");
                    return;
                }
                default -> System.out.println("Некорректный выбор.");
            }
        }
    }
}
