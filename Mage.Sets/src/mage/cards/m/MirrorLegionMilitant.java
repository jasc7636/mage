
package mage.cards.m;

import mage.MageInt;
import mage.abilities.keyword.ReplicateAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class MirrorLegionMilitant extends CardImpl {

    public MirrorLegionMilitant(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{W}");

        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SOLDIER);
        this.power = new MageInt(2);
        this.toughness = new MageInt(1);

        // Replicate {W}
        this.addAbility(new ReplicateAbility(this, "{W}"));
    }

    public MirrorLegionMilitant(final MirrorLegionMilitant card) {
        super(card);
    }

    @Override
    public MirrorLegionMilitant copy() {
        return new MirrorLegionMilitant(this);
    }
}
