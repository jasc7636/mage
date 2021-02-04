package mage.cards.s;

import mage.Mana;
import mage.abilities.dynamicvalue.common.CardsInControllerGraveyardCount;
import mage.abilities.mana.DynamicManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SuperType;
import mage.filter.StaticFilters;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class ShivsCore extends CardImpl {

    public ShivsCore(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.LAND}, "");
        addSuperType(SuperType.LEGENDARY);

        // {T}: Add {R} for each land in your graveyard.
        DynamicManaAbility ability = new DynamicManaAbility(
                Mana.RedMana(1),
                new CardsInControllerGraveyardCount(StaticFilters.FILTER_CARD_LAND)
        );
        this.addAbility(ability);
    }

    public ShivsCore(final ShivsCore card) {
        super(card);
    }

    @Override
    public ShivsCore copy() {
        return new ShivsCore(this);
    }
}