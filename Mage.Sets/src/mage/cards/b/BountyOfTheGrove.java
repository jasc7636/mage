package mage.cards.b;

import mage.MageObject;
import mage.MageObjectReference;
import mage.Mana;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.PayLifeCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.ContinuousRuleModifyingEffectImpl;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.abilities.mana.SimpleManaAbility;
import mage.cards.Card;
import mage.cards.CardSetInfo;
import mage.cards.ModalDoubleFacesCard;
import mage.constants.*;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.stack.Spell;
import mage.watchers.Watcher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author jasc7636
 */
public final class BountyOfTheGrove extends ModalDoubleFacesCard {

    public BountyOfTheGrove(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo,
                new CardType[]{CardType.SORCERY}, new SubType[]{}, "{2}{G}{G}",
                "Sheltering Grove", new CardType[]{CardType.LAND}, new SubType[]{}, ""
        );

        // 1.
        // Bounty of the Grove
        // Sorcery

        // Draw a card for each creature you control.
        this.getLeftHalfCard().getSpellAbility().addEffect(new DrawCardSourceControllerEffect(new PermanentsOnBattlefieldCount(StaticFilters.FILTER_CONTROLLED_CREATURE)));

        // 2.
        // Sheltering Grove
        // Land

        // {T}, Pay 2 life: Add {G}. If that mana is spent on a creature spell, it can't be countered.
        Mana mana = Mana.GreenMana(1);
        mana.setFlag(true); // used to indicate this mana ability
        SimpleManaAbility ability = new SimpleManaAbility(Zone.BATTLEFIELD, mana, new TapSourceCost());
        ability.addCost(new PayLifeCost(2));
        ability.getEffects().get(0).setText("Add {G}. If that mana is spent on an creature spell, that spell can't be countered");
        ability.addWatcher(new ShelteringGroveWatcher(ability.getOriginalId()));
        this.getRightHalfCard().addAbility(ability);

        this.getRightHalfCard().addAbility(new SimpleStaticAbility(Zone.ALL, new ShelteringGroveCantCounterEffect()));
    }

    private BountyOfTheGrove(final BountyOfTheGrove card) {
        super(card);
    }

    @Override
    public BountyOfTheGrove copy() {
        return new BountyOfTheGrove(this);
    }
}

class ShelteringGroveCantCounterEffect extends ContinuousRuleModifyingEffectImpl {

    public ShelteringGroveCantCounterEffect() {
        super(Duration.EndOfGame, Outcome.Benefit);
        staticText = null;
    }

    public ShelteringGroveCantCounterEffect(final ShelteringGroveCantCounterEffect effect) {
        super(effect);
    }

    @Override
    public ShelteringGroveCantCounterEffect copy() {
        return new ShelteringGroveCantCounterEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public String getInfoMessage(Ability source, GameEvent event, Game game) {
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (sourceObject != null) {
            return "This spell can't be countered because mana from " + sourceObject.getName() + " was spent to cast it.";
        }
        return null;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.COUNTER;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        ShelteringGroveWatcher watcher = game.getState().getWatcher(ShelteringGroveWatcher.class, source.getSourceId());
        Spell spell = game.getStack().getSpell(event.getTargetId());
        return spell != null && watcher != null && watcher.spellCantBeCountered(new MageObjectReference(spell, game));
    }
}

class ShelteringGroveWatcher extends Watcher {

    private final Set<MageObjectReference> spells = new HashSet<>();
    private final UUID originalId;

    public ShelteringGroveWatcher(UUID originalId) {
        super(WatcherScope.CARD);
        this.originalId = originalId;
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.MANA_PAID) {
            if (event.getData() != null && event.getData().equals(originalId.toString()) && event.getTargetId() != null) {
                Card spell = game.getSpell(event.getTargetId());
                if (spell != null && spell.isCreature()) {
                    spells.add(new MageObjectReference(game.getObject(event.getTargetId()), game));
                }
            }
        }
    }

    public boolean spellCantBeCountered(MageObjectReference mor) {
        return spells.contains(mor);
    }

    @Override
    public void reset() {
        super.reset();
        spells.clear();
    }
}