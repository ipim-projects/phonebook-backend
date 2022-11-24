package ru.ipim.phonebook.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Date;

import javax.annotation.PostConstruct;

// Конфигурация рабочих каталогов маршрута
@Component
public class DirSettings<T> {
    private static final Logger log = LoggerFactory.getLogger(DirSettings.class);

    // настройка каталог по умолчанию
    public static final String DEFAULT_DIRECTORY_SETTING = "phonebook.defaultDir";
    // настройка каталог выгрузки файлов в маршрут
    public static final String WORK_DIRECTORY_SETTING = "phonebook.workDir";

    // Свойства системы
    @Autowired
    private Environment env;

    // каталог по умолчанию
    private Path defaultDir;

    // путь для выгрузки файлов в маршрут
    private Path workDir;

    public String getWorkDirectory() {
        return workDir().toString();
    }

    // Получить имя файла с маской integration_ddMMyy_hhmm
    public Path getOutFileName() {
        final String filename = String.format("integration_%1$td%1$tm%1$ty_%1$tH%1$tM", new Date());
        Path file = getProcessPath(filename);
        return file;

    }

    @PostConstruct
    public void configureSettings() throws IOException {
        workDir = configureDirectory(WORK_DIRECTORY_SETTING, "work");
        log.info("Каталог для экспорта файлов в маршрут: {}", workDir.toString());
    }

    public Path getProcessPath(CharSequence subPath) {
        Objects.requireNonNull("", "передан пустой subPath");
        return workDir.resolve(subPath.toString());
    }

    protected Path configureDirectory(String propertyName, String defaultDirName)
            throws IOException {

        Objects.requireNonNull(propertyName, "передан пустой propertyName");
        Objects.requireNonNull(propertyName, "передан пустой defaultDirName");

        final String value = env().getProperty(propertyName);
        Path path;
        if (!StringUtils.isBlank(value)) {
            final Path tmp = Paths.get(value);
            if (tmp.isAbsolute()) {
                path = tmp;
            } else {
                path = defaultDir().resolve(tmp);
            }
        } else {
            path = defaultDir().resolve(defaultDirName);
        }

        // Создать каталог, если его нет
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path;
    }

    protected final Environment env() {
        return env;
    }

    protected final Path workDir() {
        return workDir;
    }

    protected final Path defaultDir() throws IOException {
        //
        if (defaultDir != null) {
            return defaultDir;
        }

        defaultDir = configureDefaultDir();
        log.info("Каталог по-умолчанию: {}", defaultDir.toString());
        return defaultDir;
    }

    // каталог по-умолчанию
    protected Path configureDefaultDir() throws IOException {
        // Извлечь настройку
        final Path defaultPath;
        final String value = env().getProperty(DEFAULT_DIRECTORY_SETTING);

        // получить путь
        if (!StringUtils.isBlank(value)) {
            defaultPath = Paths.get(value).toAbsolutePath();

        } else {
            // получить домашний каталог пользователя
            final String homeDir = env().getRequiredProperty("user.home");
            final String workDirName = ".Phonebook";
            // user.home Phonebook
            defaultPath = Paths.get(homeDir, workDirName);
        }
        // содать каталог, если его нет
        Files.createDirectories(defaultPath);
        return defaultPath;
    }

}
