import helpers.CustomZip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NinthHomeworkTest {

    private static CustomZip customZip = new CustomZip();

    /**
     * Упаковать файлы в zip-архив
     */
    @Test
    void packFilesIntoZipArchive() {
        String filepath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "dataForNinthHomework";
        File packageForPack = new File(filepath);
        List<File> files = new ArrayList<>();

        customZip.getAllFileInList(packageForPack, files);
        customZip.packListWithFilesIntoZipArchive(packageForPack, files);

        var expectedPath = Paths.get(new CustomZip().getPackageForZip() + File.separator + "dataForNinthHomework.zip");
        Assertions.assertTrue(Files.exists(expectedPath));
    }

    /**
     * Распоковать zip-архив
     */
    @Test
    void unpackZipArchive() {
        customZip.unpackZipArchive("src/test/resources/dataForNinthHomework");

        var expectedPath = Paths.get(new CustomZip().getPackageForZip() + File.separator + "csv" + File.separator + "Телефонная книга.csv");
        var expectedPathPdf = Paths.get(new CustomZip().getPackageForZip() + File.separator + "pdf" + File.separator + "Телефонная книга.pdf");
        var expectedPathXlsx = Paths.get(new CustomZip().getPackageForZip() + File.separator + "xlsx" + File.separator + "Телефонная книга.xlsx");
        Assertions.assertTrue(Files.exists(expectedPath));
        Assertions.assertTrue(Files.exists(expectedPathPdf));
        Assertions.assertTrue(Files.exists(expectedPathXlsx));
    }
}
