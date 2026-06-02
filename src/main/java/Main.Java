import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String BASE_PATH = "/storage/emulated/0/PROJECT_BAM/";
    public static final String ORIGINAL_FILES = BASE_PATH + "ORIGINAL_TEST_FILES";
    public static final String TEST_ENV = BASE_PATH + "TEST_ENVIRONMENT";
    public static final String FILES_TO_SCAN = BASE_PATH + "FILES_TO_SCAN";
    public static final String BACKUP_PROJECT = BASE_PATH + "BACKUP_FILES/PROJECT_BACKUP";
    public static final String BACKUP_MOBILE = BASE_PATH + "BACKUP_FILES/MOBILE_BACKUP";
    public static final String MOBILE_STORAGE = "/storage/emulated/0/";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("===== PROJECT BAM STARTED =====");

        try {
            System.out.println("\n[1] Preparing safe environment...");
            FileManager.prepareTestEnvironment(ORIGINAL_FILES, TEST_ENV);

            System.out.print("\nScan [1] Single file OR [2] Full Mobile Device? (1/2): ");
            String option = input.nextLine();

            if (option.equals("1")) {
                System.out.print("Enter file name from FILES_TO_SCAN folder: ");
                String fileName = input.nextLine();
                String filePath = FILES_TO_SCAN + "/" + fileName;

                if (!new java.io.File(filePath).exists()) {
                    System.out.println("⚠️ File not found!");
                    return;
                }

                FileManager.copyFileToTest(filePath, TEST_ENV);
                System.out.println("🔍 Analyzing file... please wait 10 seconds");
                Thread.sleep(10000);

                Detector.ScanResult result = Detector.detectMalicious(ORIGINAL_FILES, TEST_ENV);
                System.out.println("\n" + result.report);

                if (result.isMalicious) {
                    System.out.println(Deleter.deleteMaliciousFile(TEST_ENV + "/" + fileName));
                    System.out.println(Restorer.restoreProjectFiles(ORIGINAL_FILES, TEST_ENV, BACKUP_PROJECT));
                }

            } else if (option.equals("2")) {
                System.out.println("\n[2] Scanning full mobile device...");
                if (!MobileHandler.isMobileConnected(MOBILE_STORAGE)) {
                    System.out.println("⚠️ Mobile storage not accessible");
                    return;
                }

                List<String> allFiles = MobileHandler.getAllMobileFiles(MOBILE_STORAGE);
                System.out.println("📂 Found " + allFiles.size() + " files to check");

                boolean threatFound = false;
                for (String file : allFiles) {
                    FileManager.prepareTestEnvironment(ORIGINAL_FILES, TEST_ENV);
                    FileManager.copyFileToTest(file, TEST_ENV);
                    Thread.sleep(8000);

                    Detector.ScanResult res = Detector.detectMalicious(ORIGINAL_FILES, TEST_ENV);
                    if (res.isMalicious) {
                        System.out.println("\n🚨 MALICIOUS FILE FOUND: " + file);
                        System.out.println(Deleter.deleteMaliciousFile(file));
                        System.out.println(Restorer.restoreMobileFiles(MOBILE_STORAGE, BACKUP_MOBILE));
                        threatFound = true;
                        break;
                    }
                }

                if (!threatFound) {
                    System.out.println("\n✅ No threats found in your device");
                }
            }

            Deleter.clearTestEnvironment(TEST_ENV);
            System.out.println("\n===== PROCESS FINISHED =====");

        } catch (IOException | InterruptedException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
              }
