package org.mage.test.cards.single;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * @author TheElk801
 */
public class IdolOfEnduranceTest extends CardTestPlayerBase {

    private static final String idol = "Idol of Endurance";
    private static final String key = "Voltaic Key";
    private static final String sqr = "Squire";
    private static final String glrskr = "Glory Seeker";

    @Test
    public void testIdol() {
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 5);
        addCard(Zone.HAND, playerA, idol);
        addCard(Zone.GRAVEYARD, playerA, sqr);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, idol);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, sqr);

        setStopAt(1, PhaseStep.END_TURN);
        execute();
        assertAllCommandsUsed();

        assertPermanentCount(playerA, sqr, 1);
    }

    @Test
    public void testIdol2() {
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 5);
        addCard(Zone.HAND, playerA, idol);
        addCard(Zone.GRAVEYARD, playerA, sqr);
        addCard(Zone.GRAVEYARD, playerA, glrskr);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, idol);

        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");

        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, sqr);

        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, glrskr);

        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertPermanentCount(playerA, sqr, 1);
        assertPermanentCount(playerA, glrskr, 0);
    }

    @Test
    public void testIdolTwice() {
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 8);
        addCard(Zone.BATTLEFIELD, playerA, key);
        addCard(Zone.HAND, playerA, idol);
        addCard(Zone.GRAVEYARD, playerA, sqr);
        addCard(Zone.GRAVEYARD, playerA, glrskr);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, idol);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1},", idol);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, sqr);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, glrskr);

        setStopAt(1, PhaseStep.END_TURN);
        execute();
        assertAllCommandsUsed();

        assertPermanentCount(playerA, sqr, 1);
        assertPermanentCount(playerA, glrskr, 1);
    }

    @Test
    public void testIdolTwice2() {
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 8);
        addCard(Zone.BATTLEFIELD, playerA, key);
        addCard(Zone.HAND, playerA, idol);
        addCard(Zone.GRAVEYARD, playerA, sqr);
        addCard(Zone.GRAVEYARD, playerA, glrskr);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, idol);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, sqr);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1},", idol);
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);

        activateAbility(1, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}{W}");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN, playerA);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, glrskr);

        setStopAt(1, PhaseStep.END_TURN);
        execute();
        assertAllCommandsUsed();

        assertPermanentCount(playerA, sqr, 1);
        assertPermanentCount(playerA, glrskr, 1);
    }
}
