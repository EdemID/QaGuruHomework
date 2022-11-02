import helpers.CustomZip;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
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

    @Test
    void readPdf() throws IOException {
        PDDocument document = PDDocument.load(new File("src/test/resources/dataForNinthHomework/pdf" + File.separator + "Телефонная книга.pdf"));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(1);
            String text = stripper.getText(document);
            Assertions.assertTrue(text.contains("Петров Борис Петрович 79993331122"));
        }
        document.close();
    }

    @Test
    void readXlsx() throws IOException {
        String file = "src/test/resources/dataForNinthHomework/xlsx" + File.separator + "Телефонная книга.xlsx";
        FileInputStream fs = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }

        row = sheet.getRow(2);                // строка
        XSSFCell actualName = row.getCell(1); // колонка
        String expectedFirstname = "Борис";
        Assertions.assertEquals(expectedFirstname, actualName.toString());

        String expectedNumber = "79993331122";
        row.getCell(3).setCellType(1);        //смена типа ячейки - int to string
        XSSFCell actualNumber = row.getCell(3);
        Assertions.assertEquals(expectedNumber, actualNumber.toString());
    }

    @Test
    void readCsv() {
        String filename =  "src/test/resources/dataForNinthHomework/csv" + File.separator + "Телефонная книга.csv";

        try (BufferedReader fp = new BufferedReader(new FileReader(filename))) {

            String[] cols = null;
            for (int i = 0; i < 2; i++){
                cols = fp.readLine().split("\t");
                System.out.println(cols[0] + "\t" + cols[1] + "\t" + cols[2] + "\t" + cols[cols.length - 1]);
            }

            Assertions.assertEquals("Роман", cols[1]);
            Assertions.assertEquals("79031112233", cols[3]);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
