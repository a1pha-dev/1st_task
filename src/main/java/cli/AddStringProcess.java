package cli;

import db.RepositoryImpl;
import models.StringBuffer;

import java.sql.SQLException;
import java.util.Scanner;

public class AddStringProcess {

    public static void runReverse(Scanner scanner, RepositoryImpl repository) {
        if (requireTable(repository)) return;
        StringBuffer data = inputString(scanner, "Введите строку: ");
        data.reverse();
        writeToRepository(repository, data);
    }

    public static void runConcatenate(Scanner scanner, RepositoryImpl repository) {
        if (requireTable(repository)) return;
        StringBuffer data = inputString(scanner, "Введите первую строку: ");
        System.out.print("Введите вторую строку: ");
        data.concatenate(scanner.nextLine());
        writeToRepository(repository, data);
    }

    private static StringBuffer inputString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String userInput = scanner.nextLine();

        while (userInput.length() <= 50) {
            System.out.println("Ошибка: строка должна быть длиннее 50 символов.");
            userInput = scanner.nextLine();
        }
        return new StringBuffer(userInput);
    }

    private static void writeToRepository(RepositoryImpl repository, StringBuffer data) {
        try {
            repository.insert(data);
            System.out.printf("Строка %s успешно добавлена в таблицу.\n", data.getModified());
        } catch (SQLException e) {
            System.out.printf("Ошибка при добавлении строки: %s\n", e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
        }
    }

    private static boolean requireTable(RepositoryImpl repository) {
        if (repository.getTableName().isEmpty()) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
            return true;
        }
        return false;
    }
}
