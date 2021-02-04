
package mage.cards.b;

import mage.Mana;
import mage.abilities.Ability;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.mana.SimpleManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Zone;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class BlackLocus extends CardImpl {

    public BlackLocus(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.LAND},"");
        this.subtype.add(SubType.LOCUS);

        // {tap}, Sacrifice Black Locus: Add {C}{C}{C}.
        Ability ability = new SimpleManaAbility(Zone.BATTLEFIELD, Mana.ColorlessMana(3), new TapSourceCost());
        ability.addCost(new SacrificeSourceCost());
        this.addAbility(ability);
    }

    public BlackLocus(final BlackLocus card) {
        super(card);
    }

    @Override
    public BlackLocus copy() {
        return new BlackLocus(this);
    }
}
