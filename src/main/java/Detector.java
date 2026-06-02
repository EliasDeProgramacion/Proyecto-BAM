import java.io.File;
import java.io.IOException;

public class Deleter {

    public static String deleteMaliciousFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.exists()) {
                file.delete();
                return "✅ Malicious file deleted successfully";
            } else {
                return "⚠️ File not found";
            }
        } catch (Exception e) {
            return "❌ Error deleting file: " + e.getMessage();
        }
    }

    public static void clearTestEnvironment(String envPath) throws IOException {
        File envFolder = new File(envPath);
        if (envFolder.exists()) {
            FileManager.deleteFolder(envFolder);
            envFolder.mkdirs();
        }
    }
}
