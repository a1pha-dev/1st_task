package cli;

import db.RepositoryImpl;
import models.StringBuffer;

import java.sql.SQLException;
import java.util.Scanner;

public class AddStringProcess {

    public static void run_reverse(Scanner scanner, RepositoryImpl repository) {
        StringBuffer data = inputString(scanner, "Введите строку: ");
        data.reverse();
        writeToRepository(repository, data);
    }

    public static void run_concatenate(Scanner scanner, RepositoryImpl repository) {
        StringBuffer data = inputString(scanner, "Введите первую строку: ");
        System.out.print("Введите вторую строку: ");
        data.concatenate(scanner.nextLine());
        writeToRepository(repository, data);
    }

    private static StringBuffer inputString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return new StringBuffer(scanner.nextLine());
    }

    private static void writeToRepository(RepositoryImpl repository, StringBuffer data) {
        try {
            repository.insert(data);
        } catch (SQLException e) {
            System.out.printf("Ошибка при добавлении строки: %s\n", e.getMessage());
        }
    }
}
