import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileManager {

    public static void prepareTestEnvironment(String originalPath, String testEnvPath) throws IOException {
        File testFolder = new File(testEnvPath);
        if (testFolder.exists()) deleteFolder(testFolder);
        testFolder.mkdirs();

        File originalFolder = new File(originalPath);
        File[] items = originalFolder.listFiles();

        if (items != null) {
            for (File item : items) {
                File destination = new File(testEnvPath + "/" + item.getName());
                if (item.isDirectory()) {
                    copyFolder(item, destination);
                } else {
                    Files.copy(item.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static void copyFileToTest(String filePath, String testEnvPath) throws IOException {
        File source = new File(filePath);
        File destination = new File(testEnvPath + "/" + source.getName());
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static boolean areFilesEqual(String path1, String path2) throws IOException {
        File f1 = new File(path1);
        File f2 = new File(path2);

        if (!f1.exists() || !f2.exists()) return false;
        if (f1.length() != f2.length()) return false;

        try (InputStream s1 = new FileInputStream(f1);
             InputStream s2 = new FileInputStream(f2)) {

            int a, b;
            while ((a = s1.read()) != -1) {
                b = s2.read();
                if (a != b) return false;
            }
            return true;
        }
    }

    public static void deleteFolder(File folder) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteFolder(f);
                else f.delete();
            }
        }
        folder.delete();
    }

    public static void copyFolder(File source, File destination) throws IOException {
        if (!destination.exists()) destination.mkdirs();
        File[] files = source.listFiles();

        if (files != null) {
            for (File f : files) {
                File newFile = new File(destination + "/" + f.getName());
                if (f.isDirectory()) copyFolder(f, newFile);
                else Files.copy(f.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
