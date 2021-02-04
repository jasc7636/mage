package mage.cards.m;

import mage.abilities.mana.ColorlessManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class MoxLocus extends CardImpl {

    public MoxLocus(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{0}");
        this.subtype.add(SubType.LOCUS);

        // {tap}: Add {C}.
        this.addAbility(new ColorlessManaAbility());
    }

    public MoxLocus(final MoxLocus card) {
        super(card);
    }

    @Override
    public MoxLocus copy() {
        return new MoxLocus(this);
    }
}