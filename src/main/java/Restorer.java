import java.io.File;
import java.io.IOException;

public class Restorer {

    public static String restoreProjectFiles(String originalPath, String testEnvPath, String backupPath) {
        try {
            FileManager.prepareTestEnvironment(originalPath, testEnvPath);
            return "✅ Project files restored to original state";
        } catch (IOException e) {
            return "❌ Error restoring project: " + e.getMessage();
        }
    }

    public static String restoreMobileFiles(String mobilePath, String backupMobilePath) {
        try {
            File mobileFolder = new File(mobilePath);
            File backupFolder = new File(backupMobilePath);

            if (mobileFolder.exists() && backupFolder.exists()) {
                FileManager.deleteFolder(mobileFolder);
                mobileFolder.mkdirs();
                FileManager.copyFolder(backupFolder, mobileFolder);
                return "✅ Mobile files restored successfully";
            } else {
                return "⚠️ Mobile path or backup folder not found";
            }
        } catch (IOException e) {
            return "❌ Error restoring mobile: " + e.getMessage();
        }
    }
}
