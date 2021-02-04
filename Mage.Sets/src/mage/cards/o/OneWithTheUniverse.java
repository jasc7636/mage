
package mage.cards.o;

import mage.abilities.DelayedTriggeredAbility;
import mage.abilities.effects.common.CreateDelayedTriggeredAbilityEffect;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.stack.Spell;

import java.util.UUID;

public final class OneWithTheUniverse extends CardImpl {

    public OneWithTheUniverse (UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{G}");


        // Whenever you cast a creature spell this turn, draw a card.
        this.getSpellAbility().addEffect(new CreateDelayedTriggeredAbilityEffect(new OneWithTheUniverseTriggeredAbility()));
    }

    public OneWithTheUniverse (final OneWithTheUniverse card) {
        super(card);
    }

    @Override
    public OneWithTheUniverse copy() {
        return new OneWithTheUniverse(this);
    }

}

class OneWithTheUniverseTriggeredAbility extends DelayedTriggeredAbility {

    public OneWithTheUniverseTriggeredAbility() {
        super(new DrawCardSourceControllerEffect(1), Duration.EndOfTurn, false);
    }

    public OneWithTheUniverseTriggeredAbility(OneWithTheUniverseTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.SPELL_CAST || event.getType() == EventType.PLAY_LAND;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getPlayerId().equals(this.getControllerId())) {
            if (event.getType() == EventType.PLAY_LAND) {
                return true;
            }
            Spell spell = game.getStack().getSpell(event.getTargetId());
            if (spell != null && StaticFilters.FILTER_SPELL_A_CREATURE.match(spell, game)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public OneWithTheUniverseTriggeredAbility copy() {
        return new OneWithTheUniverseTriggeredAbility(this);
    }

    @Override
    public String getRule() {
        return "Whenever you cast a creature spell or play a land this turn, " + modes.getText();
    }
}