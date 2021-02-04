
package mage.cards.g;

import mage.MageInt;
import mage.abilities.SpellAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.mana.GreenManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class GentleGardener extends CardImpl {

    public GentleGardener(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.LAND,CardType.CREATURE},"");
        this.subtype.add(SubType.FOREST);

        this.power = new MageInt(0);
        this.toughness = new MageInt(2);

        this.color.setGreen(true);

        SpellAbility spellAbility = new SpellAbility(new ManaCostsImpl("G"), name, Zone.HAND, SpellAbilityType.BASE_ALTERNATE);
        spellAbility.setTiming(TimingRule.SORCERY);
        this.addAbility(spellAbility);
        this.setSpellAbility(spellAbility);

        this.addAbility(new GreenManaAbility());
    }

    public GentleGardener(final GentleGardener card) {
        super(card);
    }

    @Override
    public GentleGardener copy() {
        return new GentleGardener(this);
    }
}
