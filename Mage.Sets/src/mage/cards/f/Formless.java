
package mage.cards.f;

import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.SpellAbility;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.costs.Cost;
import mage.abilities.costs.common.ExileFromGraveCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CopyEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.filter.StaticFilters;
import mage.filter.common.FilterPermanentCard;
import mage.filter.predicate.Predicates;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.PermanentCard;
import mage.target.common.TargetCardInYourGraveyard;
import mage.util.functions.CopyApplier;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class Formless extends CardImpl {

    private static final FilterPermanentCard nonAuraFilter = new FilterPermanentCard("non aura permanent card");

    static {
        nonAuraFilter.add(Predicates.not(SubType.AURA.getPredicate()));
    }

    public Formless(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{W}");
        this.subtype.add(SubType.SHAPESHIFTER);

        this.power = new MageInt(0);
        this.toughness = new MageInt(0);

        // When you cast Formless, you may exile target non aura permanent card in a graveyard.
        ExileFromGraveCost exileFromGraveCost = new ExileFromGraveCost(new TargetCardInYourGraveyard(0, 1, StaticFilters.FILTER_CARD_PERMANENT));
        this.getSpellAbility().addCost(exileFromGraveCost);

        // If you do, Formless enters the battlefield as a copy of that card, except it's a 2/2 creature
        this.addAbility(new EntersBattlefieldAbility(new FormlessCopyEffect(), false));
    }

    public Formless(final Formless card) {
        super(card);
    }

    @Override
    public Formless copy() {
        return new Formless(this);
    }
}

class FormlessCopyEffect extends OneShotEffect {
    // TODO Planewalker enter with double loyalty and take no creature damage
    public FormlessCopyEffect() {
        super(Outcome.Copy);
        this.staticText = "as a copy of that card, expect it's a 2/2 creature";
    }

    public FormlessCopyEffect(final FormlessCopyEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent formless = game.getPermanentEntering(source.getSourceId());
        if (formless == null) {
            return false;
        }
        if (values.containsKey("sourceCastSpellAbility") && values.get("sourceCastSpellAbility") instanceof SpellAbility) {
            SpellAbility castAbility = (SpellAbility) values.get("sourceCastSpellAbility");
            for(Cost cost : castAbility.getCosts()) {
                if (cost instanceof ExileFromGraveCost) {

                    List<Card> exiledCards = ((ExileFromGraveCost) cost).getExiledCards();
                    if (exiledCards.size() <= 0 || exiledCards.get(0) == null) {
                        return true;
                    }
                    Card copyFromCard = exiledCards.get(0);
                    if (formless.isCopy() && exiledCards.get(0) instanceof Formless) {
                        return true;
                    }
                    Permanent newBluePrint = new PermanentCard(copyFromCard, source.getControllerId(), game);
                    newBluePrint.assignNewId();
                    CopyApplier applier = new FormlessApplier();
                    applier.apply(game, newBluePrint, source, formless.getId());
                    CopyEffect copyEffect = new CopyEffect(Duration.Custom, newBluePrint, formless.getId());
                    copyEffect.newId();
                    copyEffect.setApplier(applier);
                    Ability newAbility = source.copy();
                    copyEffect.init(newAbility, game);
                    game.addEffect(copyEffect, newAbility);

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public FormlessCopyEffect copy() {
        return new FormlessCopyEffect(this);
    }
}

class FormlessApplier extends CopyApplier {

    @Override
    public boolean apply(Game game, MageObject mageObject, Ability source, UUID copyToObjectId) {
        if (!mageObject.isCreature()) {
            mageObject.addCardType(CardType.CREATURE);
        }
        mageObject.getPower().modifyBaseValue(2);
        mageObject.getToughness().modifyBaseValue(2);
        return true;
    }
}
