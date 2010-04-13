/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test set for messages.
 * 
 * @author Ivo Lasek
 *
 */
public class MessagesTest {

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.Messages#getString(java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testGetString() {
        assertEquals("Fake Game", Messages.getString("FakeGamePluginDialog.Shell.Title"));
    }

}
