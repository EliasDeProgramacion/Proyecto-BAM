import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MobileHandler {

    public static boolean isMobileConnected(String mobilePath) {
        File mobileFolder = new File(mobilePath);
        return mobileFolder.exists() && mobileFolder.canRead();
    }

    public static List<String> getAllMobileFiles(String mobilePath) {
        List<String> filesList = new ArrayList<>();
        File folder = new File(mobilePath);
        scanFolder(folder, filesList);
        return filesList;
    }

    private static void scanFolder(File folder, List<String> list) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    scanFolder(f, list);
                } else {
                    list.add(f.getAbsolutePath());
                }
            }
        }
    }
}
