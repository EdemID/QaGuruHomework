import helpers.MyArgumentsProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

public class EighthHomeworkTest {

    @ParameterizedTest
    @DisplayName("Null и пустая строка")
    @NullSource
    @EmptySource
    @ValueSource(strings = { " ", "   ", "\t", "\n" })
    void nullEmptyAndBlankStrings(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    @ParameterizedTest
    @DisplayName("С указанием value, так как указан тип параметра интерфейс")
    @EnumSource(value = ChronoUnit.class)
    void testWithEnumSource(TemporalUnit unit) {
        assertNotNull(unit);
    }

    @ParameterizedTest
    @DisplayName("Без указания value, так как указан тип параметра enum")
    @EnumSource
    void testWithEnumSource(ChronoUnit unit) {
        assertNotNull(unit);
    }


    @ParameterizedTest
    @DisplayName("С заданными названиями enum")
    @EnumSource(names = { "DAYS", "HOURS" })
    void testWithEnumSourceInclude(ChronoUnit unit) {
        Set<ChronoUnit> units = EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS);
        assertTrue(units.contains(unit));
    }

    @ParameterizedTest
    @DisplayName("С указанием атрибута mode: исключить enum, названия которых перечислены")
    @EnumSource(mode = EXCLUDE, names = { "ERAS", "FOREVER" })
    void testWithEnumSourceExclude(ChronoUnit unit) {
        assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
    }


    @ParameterizedTest
    @DisplayName("С указанием атрибута mode: параметры, удовлетворяющие регулярное выражение")
    @EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
    void testWithEnumSourceRegex(ChronoUnit unit) {
        assertTrue(unit.name().endsWith("DAYS"));
    }

    @ParameterizedTest
    @DisplayName("MethodSource: один параметр и несколько тестовых данных в Stream.of()")
    @MethodSource("helpers.TestData#testWithExplicitMethodSource")
    void testWithExplicitMethodSource(String argument) {
        assertNotNull(argument);
    }

    @ParameterizedTest
    @DisplayName("MethodSource: несколько параметров и аргументов в Arguments.of() и Arguments.arguments()")
    @MethodSource("helpers.TestData#stringIntAndListProvider")
    void testWithMultiArgMethodSource(String str, int num, List<String> list) {
        assertEquals(5, str.length());
        assertTrue(num >=1 && num <=2);
        assertEquals(2, list.size());
    }

    @ParameterizedTest
    @DisplayName("Один String - одна строка в csv")
    @CsvSource({
            "apple,         1",
            "banana,        2",
            "'lemon, lime', 0xF1",
            "strawberry,    700_000"
    })
    void testWithCsvSource(String fruit, int rank) {
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }

    @ParameterizedTest
    @DisplayName("В качестве разделителя задан String, который входит в строку")
    @CsvSource(delimiterString = ":n", value = {
            "apple             :n  1",
            "'  lemon, lime  ' :n  0xF1",
            "strawberry        :n  700_000"
    })
    void testWithCsvSourceWithDelimiterString(String fruit, int rank) {
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }

    @ParameterizedTest
    @DisplayName("Все пробелы сохраняются - чувствительный к пробелам. В качестве разделителя задан ':' типа Char (HEX), который входит в строку")
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, delimiter = 0x3A, value = {
            "  lemon, lime  :1",
            "'  lemon, lime  ' :0xF1"
    })
    void testWithCsvSourceWithIgnoreLeadingAndTrailingWhitespace(String fruit, int rank) {
        assertEquals("  lemon, lime  ", fruit);
        assertNotEquals(0, rank);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromClasspath(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromFile(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    /**
     * Первая строка в csv файле участвыет в названии теста, где index - это номер теста, а arguments - строки из csv
     * @param country
     * @param reference
     */
    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvFileSource(resources = "/two-column.csv", useHeadersInDisplayName = true)
    void testWithCsvFileSourceAndHeaders(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest
    @DisplayName("ArgumentSource: один параметр и несколько тестовых данных в Stream.of()")
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        assertNotNull(argument);
    }
}
