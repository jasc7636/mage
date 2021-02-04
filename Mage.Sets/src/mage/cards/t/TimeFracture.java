
package mage.cards.t;

import mage.abilities.effects.common.EndTurnEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class TimeFracture extends CardImpl {

    public TimeFracture(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{1}{W}");


        // End the turn.
        this.getSpellAbility().addEffect(new EndTurnEffect());
    }

    public TimeFracture(final TimeFracture card) {
        super(card);
    }

    @Override
    public TimeFracture copy() {
        return new TimeFracture(this);
    }
}
