package habi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests command parsing and validation. */
public class ParserTest {
    @Test
    public void parseDeadline_validIsoDate_returnsDeadline() throws HabiException {
        Deadline deadline = Parser.parseDeadline(
                "deadline return book /by 2026-09-15");

        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 15), deadline.getBy());
    }

    @Test
    public void parseDeadline_invalidDate_throwsHabiException() {
        HabiException exception = assertThrows(HabiException.class,
                () -> Parser.parseDeadline("deadline return book /by 15-09-2026"));

        assertEquals("OOPS! Use: deadline DESCRIPTION /by yyyy-MM-dd",
                exception.getMessage());
    }

    @Test
    public void parseTaskIndex_validAndInvalidNumbers_handlesOneBasedIndex()
            throws HabiException {
        assertEquals(1, Parser.parseTaskIndex("mark 2", "mark", 3));
        assertThrows(HabiException.class,
                () -> Parser.parseTaskIndex("mark 4", "mark", 3));
    }

    @Test
    public void parseFindKeyword_presentAndMissingKeyword_handlesBothCases()
            throws HabiException {
        assertEquals("book", Parser.parseFindKeyword("find book"));
        assertThrows(HabiException.class,
                () -> Parser.parseFindKeyword("find"));
    }
}
