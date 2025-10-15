import db.DatabaseUtil;
import excel.ExcelExporter;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String currentTable = null;

        while (true) {
            System.out.println("""
                        \nВыберите действие:
                        1. Показать все таблицы
                        2. Подключиться или создать таблицу
                        3. Развернуть строку
                        4. Конкатенировать строки
                        5. Экспортировать в Excel
                        0. Выйти
                    """);

            switch (scanner.nextLine().trim()) {
                case "1" -> DatabaseUtil.printAllTables();
                case "2" -> currentTable = DatabaseUtil.connectOrCreateTable(scanner);
                case "3" -> {
                    if (requireTable(currentTable)) addString(scanner, currentTable, true);
                }
                case "4" -> {
                    if (requireTable(currentTable)) addString(scanner, currentTable, false);
                }
                case "5" -> {
                    if (requireTable(currentTable)) export(currentTable);
                }
                case "0" -> {
                    System.out.println("Выход.");
                    return;
                }
                default -> System.out.println("Некорректный выбор.");
            }
        }
    }

    private static boolean requireTable(String table) {
        if (table == null) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
            return false;
        }
        return true;
    }

    private static void addString(Scanner scanner, String table, Boolean reverse) {
        System.out.print("Введите строку: ");
        String s1 = scanner.nextLine();

        String modified;
        if (reverse) {
            modified = new StringBuilder(s1).reverse().toString();
        } else {
            System.out.print("Введите вторую строку: ");
            String s2 = scanner.nextLine();
            modified = s1 + s2;
        }

        DatabaseUtil.insertString(table, s1, modified);
        System.out.printf("\nСтрока %s добавлена.", modified);
    }

    private static void export(String table) {
        ExcelExporter.exportToExcel(table, DatabaseUtil.getAllRows(table));
    }
}
