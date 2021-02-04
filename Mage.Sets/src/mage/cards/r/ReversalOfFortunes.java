
package mage.cards.r;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetOpponent;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class ReversalOfFortunes extends CardImpl {

    public ReversalOfFortunes(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{W}");


        // If target opponent has more cards in hand than you, draw cards equal to the difference.
        this.getSpellAbility().addEffect(new ReversalOfFortunesEffect());
        this.getSpellAbility().addTarget(new TargetOpponent());
    }

    public ReversalOfFortunes(final ReversalOfFortunes card) {
        super(card);
    }

    @Override
    public ReversalOfFortunes copy() {
        return new ReversalOfFortunes(this);
    }
}

class ReversalOfFortunesEffect extends OneShotEffect {

    public ReversalOfFortunesEffect() {
        super(Outcome.DrawCard);
        this.staticText = "If target opponent has more cards in hand than you, draw cards equal to the difference";
    }

    public ReversalOfFortunesEffect(final ReversalOfFortunesEffect effect) {
        super(effect);
    }

    @Override
    public ReversalOfFortunesEffect copy() {
        return new ReversalOfFortunesEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        Player opponent = game.getPlayer(source.getFirstTarget());

        if (opponent != null && player != null && opponent.getHand().size() > player.getHand().size()) {
            player.drawCards(opponent.getHand().size() - player.getHand().size(), source, game);
            return true;
        }

        return false;
    }
}
