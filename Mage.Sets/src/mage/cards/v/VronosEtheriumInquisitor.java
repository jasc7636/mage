
package mage.cards.v;

import mage.abilities.LoyaltyAbility;
import mage.abilities.common.PlaneswalkerEntersWithLoyaltyCountersAbility;
import mage.abilities.effects.common.*;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SuperType;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.game.permanent.token.AngelToken;
import mage.target.Target;
import mage.target.TargetPermanent;
import mage.target.common.TargetCreatureOrPlaneswalker;

import java.util.UUID;

/**
 * @author jasc7636
 */
public final class VronosEtheriumInquisitor extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("noncreature permanent");

    static {
        filter.add(Predicates.not(CardType.CREATURE.getPredicate()));
        filter.add(Predicates.not(CardType.LAND.getPredicate()));
    }

    public VronosEtheriumInquisitor(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.PLANESWALKER}, "{W}{U}");
        this.addSuperType(SuperType.LEGENDARY);
        //this.subtype.add(SubType.VRONOS);

        this.addAbility(new PlaneswalkerEntersWithLoyaltyCountersAbility(2));

        // +1: Exile another target nonland, noncreature permanent, then return it to the battlefield under its owner's control
        LoyaltyAbility ability1 = new LoyaltyAbility(new ExileTargetForSourceEffect(), 1);
        ability1.addEffect(new ReturnToBattlefieldUnderOwnerControlTargetEffect(false, false).concatBy("then"));
        Target target = new TargetPermanent(new TargetPermanent(filter));
        ability1.addTarget(target);
        this.addAbility(ability1);

        // -3: Exile Vronos and target creature or planeswalker.
        LoyaltyAbility ability2 = new LoyaltyAbility(new ExileSourceEffect(), -3);
        ability2.addEffect(new ExileTargetEffect().setText("and target permanent"));
        ability2.addTarget(new TargetCreatureOrPlaneswalker());
        this.addAbility(ability2);

        // -6: Create two 4/4 white Angel tokens with flying
        LoyaltyAbility ability3 = new LoyaltyAbility(new CreateTokenEffect(new AngelToken()), -8);
        this.addAbility(ability3);
    }

    public VronosEtheriumInquisitor(final VronosEtheriumInquisitor card) {
        super(card);
    }

    @Override
    public VronosEtheriumInquisitor copy() {
        return new VronosEtheriumInquisitor(this);
    }

}

