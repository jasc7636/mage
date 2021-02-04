package mage.cards.p;

import mage.abilities.Ability;
import mage.abilities.effects.AsThoughEffectImpl;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.continuous.PlayAdditionalLandsControllerEffect;
import mage.abilities.keyword.StormAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetCardInGraveyard;
import mage.target.targetpointer.FixedTarget;
import mage.util.CardUtil;

import java.util.UUID;

/**
 * @author emerald000
 */
public final class PullOfTheWilderness extends CardImpl {

    public PullOfTheWilderness(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{2}{R}{G}");

        // Exile target card from your graveyard. You may play the exiled card until the end of your next turn.
        this.getSpellAbility().addEffect(new PullOfTheWildernessEffect());
        this.getSpellAbility().addTarget(new TargetCardInGraveyard(StaticFilters.FILTER_CARD_FROM_YOUR_GRAVEYARD));

        // You may play an additional land this turn.
        this.getSpellAbility().addEffect(new PlayAdditionalLandsControllerEffect(1, Duration.EndOfTurn));

        // Storm
        this.addAbility(new StormAbility());
    }

    public PullOfTheWilderness(final PullOfTheWilderness card) {
        super(card);
    }

    @Override
    public PullOfTheWilderness copy() {
        return new PullOfTheWilderness(this);
    }
}

class PullOfTheWildernessEffect extends OneShotEffect {

    PullOfTheWildernessEffect() {
        super(Outcome.PlayForFree);
        this.staticText = "Exile target card from your graveyard. You may play the exiled card until the end of your next turn";
    }

    private PullOfTheWildernessEffect(final PullOfTheWildernessEffect effect) {
        super(effect);
    }

    @Override
    public PullOfTheWildernessEffect copy() {
        return new PullOfTheWildernessEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller == null) {
            return false;
        }

        Card card = game.getCard(targetPointer.getFirst(game, source));
        controller.moveCards(card, Zone.EXILED, source, game);
        ContinuousEffect effect = new PullOfTheWildernessMayPlayEffect();
        effect.setTargetPointer(new FixedTarget(card.getId()));
        game.addEffect(effect, source);
        return true;
    }
}

class PullOfTheWildernessMayPlayEffect extends AsThoughEffectImpl {

    private int castOnTurn = 0;

    PullOfTheWildernessMayPlayEffect() {
        super(AsThoughEffectType.PLAY_FROM_NOT_OWN_HAND_ZONE, Duration.Custom, Outcome.Benefit);
        this.staticText = "Until the end of your next turn, you may play that card.";
    }

    private PullOfTheWildernessMayPlayEffect(final PullOfTheWildernessMayPlayEffect effect) {
        super(effect);
        castOnTurn = effect.castOnTurn;
    }

    @Override
    public PullOfTheWildernessMayPlayEffect copy() {
        return new PullOfTheWildernessMayPlayEffect(this);
    }

    @Override
    public void init(Ability source, Game game) {
        super.init(source, game);
        castOnTurn = game.getTurnNum();
    }

    @Override
    public boolean isInactive(Ability source, Game game) {
        return castOnTurn != game.getTurnNum()
                && game.getPhase().getStep().getType() == PhaseStep.END_TURN
                && game.isActivePlayer(source.getControllerId());
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean applies(UUID sourceId, Ability source, UUID affectedControllerId, Game game) {
        UUID objectIdToCast = CardUtil.getMainCardId(game, sourceId);
        return source.isControlledBy(affectedControllerId)
                && getTargetPointer().getTargets(game, source).contains(objectIdToCast);
    }
}