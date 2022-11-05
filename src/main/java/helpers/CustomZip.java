package helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

public class CustomZip {

    private Path packageForZip = Paths.get("build" + File.separator + "packageForZip");
    private String zipArchiveName;
    
    /**
     * Добавить все файлы в список файлов
     *
     * @param packageForPack пакет, в котором находятся файлы, которые необходимо архивировать
     * @param files          список, в который будут добавлены файлы
     */
    public void getAllFileInList(File packageForPack, List<File> files) {
        File[] filesInFolder = packageForPack.listFiles();
        assert filesInFolder != null;
        for (File file : filesInFolder) {
            if (file.isDirectory()) {
                getAllFileInList(file, files);
            } else {
                files.add(file);
            }
        }
    }

    /**
     * Упаковать список файлов в zip-архив
     *
     * @param packageForPack пакет, в котором находятся файлы, которые необходимо архивировать
     * @param files          список файлов, которые будут архивированы
     */
    public void packListWithFilesIntoZipArchive(File packageForPack, List<File> files) {
        if (Files.notExists(packageForZip)) {
            packageForZip.toFile().mkdir();
        }

        zipArchiveName = packageForZip + File.separator + packageForPack.getName() + ".zip";

        try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipArchiveName))) {
            for (File file : files) {
                if (file.isFile()) {
                    addFileToZip(file, zipStream, packageForPack);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Файл не найден: " + e.getMessage());
        }
    }

    /**
     * Добавить файл в zip-архив
     *
     * Для записи файлов в архив для каждого файла создается объект ZipEntry,
     * в конструктор которого передается имя архивируемого файла.
     * А чтобы добавить каждый объект ZipEntry в архив, применяется метод putNextEntry().
     *
     * nameOfArchivedFile - влияет на структуру в zip-архиве
     * file.getCanonicalPath().substring(packageForPack.getCanonicalPath().length() + 1) - source root
     * file.getPath() - repository root
     * file.getName() - только файлы (без папок)
     */
    private void addFileToZip(File file, ZipOutputStream zipOutputStream, File packageForPack) throws IOException {
        String nameOfArchivedFile = file.getCanonicalPath().substring(packageForPack.getCanonicalPath().length() + 1);
        ZipEntry zipEntry = new ZipEntry(nameOfArchivedFile);
        zipOutputStream.putNextEntry(zipEntry);

        try (FileInputStream fileReadStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fileReadStream.read(buffer)) >= 0) {
                zipOutputStream.write(buffer, 0, length);
            }
        }
    }

    /**
     * Распаковать zip-архив
     */
    public void unpackZipArchive(String packageWithZip) {
        if (Files.notExists(packageForZip)) {
            packageForZip.toFile().mkdir();
        }
        zipArchiveName = getFilenamesWithSpecificExtensionFromDirectory(packageWithZip, "zip").get(0);
        try (var file = new ZipFile(zipArchiveName)) {
            var entries = file.entries();
            var uncompressedDirectory = packageForZip + File.separator;
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                if (entry.isDirectory()) {
                    processDirectory(uncompressedDirectory, entry);
                } else {
                    processFile(file, uncompressedDirectory, entry);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Zip-архив не найден: " + e.getMessage());
        }
    }

    /**
     * Обработка директории
     *
     * @param uncompressedDirectory  директория для распаковки
     * @param entry                  сущность zip-архива
     */
    private void processDirectory(String uncompressedDirectory, ZipEntry entry) {
        var newDirectory = uncompressedDirectory + entry.getName();
        System.out.println("Creating Directory: " + newDirectory);
        var directory = new File(newDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Обработка файла
     *
     * @param file                   zip-архив
     * @param uncompressedDirectory  директория для распаковки
     * @param entry                  сущность zip-архива
     */
    private void processFile(ZipFile file, String uncompressedDirectory, ZipEntry entry) {
        try (
                var is = file.getInputStream(entry);
                var bis = new BufferedInputStream(is)
        ) {
            var uncompressedFileName = uncompressedDirectory + entry.getName();
            try (
                    var os = new FileOutputStream(uncompressedFileName);
                    var bos = new BufferedOutputStream(os)
            ) {
                while (bis.available() > 0) {
                    bos.write(bis.read());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Файл в zip-архиве не найден: " + e.getMessage());
        }
    }

    /**
     * Получить список наименований файлов определенного расширения в заданной директорий
     *
     * @param  path  путь до директории
     * @param  ext   расширение искомых файлов
     * @return       список наименовай файлов
     */
    private List<String> getFilenamesWithSpecificExtensionFromDirectory(String path, String ext) {
        List<String> filenames = new ArrayList<>();
        try (Stream<Path> filesInDirectory = Files.walk(Paths.get(path), FOLLOW_LINKS)) {
            filesInDirectory.forEach(file -> {
                        if(file.toFile().isFile() && file.toFile().getPath().endsWith("." + ext)){
                            filenames.add(file.toString());
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Пакет не найдена: " + e.getMessage());
        }
        return filenames;
    }

    public Path getPackageForZip() {
        return packageForZip;
    }
}
