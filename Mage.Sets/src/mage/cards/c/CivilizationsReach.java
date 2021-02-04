package mage.cards.c;

import mage.abilities.Ability;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.FlashbackAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.TimingRule;
import mage.constants.Zone;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.stack.Spell;
import mage.players.Player;
import mage.target.common.TargetCardInLibrary;
import mage.target.common.TargetControlledPermanent;

import java.util.UUID;

/**
 * @author jasc7636
 */
public final class CivilizationsReach extends CardImpl {

    public CivilizationsReach(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.INSTANT}, "{1}{G}");

        // Search your library for a basic land card and put that card onto the battlefield tapped.
        // Then shuffle your library. If you cast Civilization's Reach from your graveyard, search your library for a land card instead.
        this.getSpellAbility().addEffect(new CivilizationsReachEffect());

        // Flashback {G}, Sacrifice a land.
        Ability ability = new FlashbackAbility(new SacrificeTargetCost(new TargetControlledPermanent(StaticFilters.FILTER_CONTROLLED_PERMANENT_LAND)), TimingRule.INSTANT);
        ability.addManaCost(new ManaCostsImpl("{G}"));
        this.addAbility(ability);
    }

    private CivilizationsReach(final CivilizationsReach card) {
        super(card);
    }

    @Override
    public CivilizationsReach copy() {
        return new CivilizationsReach(this);
    }
}

class CivilizationsReachEffect extends OneShotEffect {

    CivilizationsReachEffect() {
        super(Outcome.Benefit);
        staticText = "Search your library for a basic land card and put that card onto the battlefield tapped." +
                "Then shuffle your library. If you cast Civilization's Reach from your graveyard, search your library for a land card instead.";
    }

    private CivilizationsReachEffect(final CivilizationsReachEffect effect) {
        super(effect);
    }

    @Override
    public CivilizationsReachEffect copy() {
        return new CivilizationsReachEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        Spell sourceSpell = game.getSpell(source.getSourceId());
        boolean castFromGraveyard = sourceSpell.getFromZone() == Zone.GRAVEYARD;
        TargetCardInLibrary target = new TargetCardInLibrary(castFromGraveyard ? StaticFilters.FILTER_CARD_LAND : StaticFilters.FILTER_CARD_BASIC_LAND);
        if (player.searchLibrary(target, source, game)) {
            if (!target.getTargets().isEmpty()) {
                player.moveCards(new CardsImpl(target.getTargets()).getCards(game),
                        Zone.BATTLEFIELD, source, game, !castFromGraveyard, false, false, null);
            }
        }
        return true;
    }
}
