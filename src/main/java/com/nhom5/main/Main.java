package com.nhom5.main;

import com.nhom5.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Điểm chạy của chương trình.
 * Giao diện là Java Swing, đúng chủ đề lập trình giao diện đồ họa của học phần.
 * Cách chạy: mvn compile exec:java
 */
public class Main {

    public static void main(String[] args) {
        Path dataDirectory = Path.of("data");
        if (!Files.exists(dataDirectory.resolve("seed").resolve("rooms.csv"))) {
            System.err.println("Hay chay lenh tai thu muc goc cua project, noi co file pom.xml va thu muc data.");
            System.exit(1);
        }

        AppContext app = AppContext.open(dataDirectory.resolve("studyroom.db"), dataDirectory, true);
        SwingUtilities.invokeLater(() -> openWindow(app));
    }

    private static void openWindow(AppContext app) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Dung giao dien mac dinh cua Java.");
        }
        new MainFrame(app).setVisible(true);
    }
}
