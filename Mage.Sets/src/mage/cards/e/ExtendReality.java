package mage.cards.e;

import mage.MageObjectReference;
import mage.abilities.Ability;
import mage.abilities.DelayedTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateDelayedTriggeredAbilityEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.stack.Spell;
import mage.game.stack.StackObject;
import mage.watchers.common.CastSpellLastTurnWatcher;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class ExtendReality extends CardImpl {

    public ExtendReality(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{U}{R}{G}");

        // The next spell you cast this turn has Storm.
        this.getSpellAbility().addEffect(
                new CreateDelayedTriggeredAbilityEffect(new ExtendRealityAbility())
                        .setText("The next spell you cast this turn has Storm")
        );
    }

    public ExtendReality(final ExtendReality card) {
        super(card);
    }

    @Override
    public ExtendReality copy() {
        return new ExtendReality(this);
    }
}

class ExtendRealityAbility extends DelayedTriggeredAbility {


    public ExtendRealityAbility() {
        super(new StormEffect(), Duration.EndOfTurn);
    }

    public ExtendRealityAbility(final ExtendRealityAbility ability) {
        super(ability);
    }

    @Override
    public ExtendRealityAbility copy() {
        return new ExtendRealityAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.SPELL_CAST;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getPlayerId().equals(this.getControllerId())) {
            StackObject spell = game.getStack().getSpell(event.getTargetId());
            if (spell instanceof Spell) {
                for (Effect effect : this.getEffects()) {
                    effect.setValue("StormSpell", spell);
                    effect.setValue("StormSpellRef", new MageObjectReference(spell.getId(), game));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "The next spell you cast this turn has Storm.";
    }
}

class StormEffect extends OneShotEffect {

    public StormEffect() {
        super(Outcome.Copy);
    }

    public StormEffect(final StormEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        MageObjectReference spellRef = (MageObjectReference) this.getValue("StormSpellRef");
        if (spellRef != null) {
            CastSpellLastTurnWatcher watcher = game.getState().getWatcher(CastSpellLastTurnWatcher.class);
            if (watcher != null) {
                int stormCount = watcher.getSpellOrder(spellRef, game) - 1;
                if (stormCount > 0) {
                    Spell spell = (Spell) this.getValue("StormSpell");
                    if (spell != null) {
                        if (!game.isSimulation()) {
                            game.informPlayers("Storm: " + spell.getLogName() + " will be copied " + stormCount + " time" + (stormCount > 1 ? "s" : ""));
                        }
                        spell.createCopyOnStack(game, source, source.getControllerId(), true, stormCount);
                    }
                }
            } else {
                Logger.getLogger(StormEffect.class).fatal("CastSpellLastTurnWatcher not found. game = " + game.getGameType().toString());
            }
            return true;
        }
        return false;
    }

    @Override
    public StormEffect copy() {
        return new StormEffect(this);
    }
}
