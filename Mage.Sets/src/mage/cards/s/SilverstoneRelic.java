package mage.cards.s;

import mage.abilities.Ability;
import mage.abilities.SpellAbility;
import mage.abilities.StaticAbility;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.*;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.ReplacementEffectImpl;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.abilities.effects.common.ReturnToHandSourceEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.players.Player;

import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author fireshoes
 */
public final class SilverstoneRelic extends CardImpl {

    public SilverstoneRelic(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{1}");

        // Buyback-Scry 1.
        this.addAbility(new SilverstoneRelicBuybackAbility(new ScryCost()));

        // When Silverstone Relic enters the battlefield, draw a card.
        this.addAbility(new EntersBattlefieldAbility(new DrawCardSourceControllerEffect(1)));

        // {5}: Return Silverstone Relic to it's owner's hand.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new ReturnToHandSourceEffect(true), new GenericManaCost(5)));
    }

    public SilverstoneRelic(final SilverstoneRelic card) {
        super(card);
    }

    @Override
    public SilverstoneRelic copy() {
        return new SilverstoneRelic(this);
    }
}

class ScryCost extends CostImpl {

    public ScryCost() {
        super();
        this.text = "Scry 1";
    }

    public ScryCost(ScryCost cost) {
        super(cost);
    }

    @Override
    public boolean pay(Ability ability, Game game, Ability source, UUID controllerId, boolean noMana, Cost costToPay) {
        Player player = game.getPlayer(controllerId);
        if (player == null) {
            return false;
        }
        player.scry(1, ability, game);
        this.paid = true;
        return true;
    }

    @Override
    public boolean canPay(Ability ability, Ability source, UUID controllerId, Game game) {
        return true;
    }

    @Override
    public ScryCost copy() {
        return new ScryCost(this);
    }
}

class SilverstoneRelicBuybackAbility extends StaticAbility implements OptionalAdditionalSourceCosts {

    private static final String keywordText = "Buyback";
    private static final String reminderTextCost = "You may {cost} in addition to any other costs as you cast this spell. If you do, put this card into your hand as it resolves.";
    private static final String reminderTextMana = "You may pay an additional {cost} as you cast this spell. If you do, put this card into your hand as it resolves.";
    protected OptionalAdditionalCost buybackCost;
    private int amountToReduceBy = 0;

    public SilverstoneRelicBuybackAbility(String manaString) {
        super(Zone.STACK, new SilverstoneRelicBuybackEffect());
        this.buybackCost = new OptionalAdditionalCostImpl(keywordText, reminderTextMana, new ManaCostsImpl(manaString));
        setRuleAtTheTop(true);
    }

    public SilverstoneRelicBuybackAbility(Cost cost) {
        super(Zone.STACK, new SilverstoneRelicBuybackEffect());
        this.buybackCost = new OptionalAdditionalCostImpl(keywordText, "&mdash;", reminderTextCost, cost);
        setRuleAtTheTop(true);
    }

    public SilverstoneRelicBuybackAbility(final SilverstoneRelicBuybackAbility ability) {
        super(ability);
        buybackCost = new OptionalAdditionalCostImpl((OptionalAdditionalCostImpl) ability.buybackCost);
        amountToReduceBy = ability.amountToReduceBy;
    }

    @Override
    public SilverstoneRelicBuybackAbility copy() {
        return new SilverstoneRelicBuybackAbility(this);
    }

    @Override
    public void addCost(Cost cost) {
        ((OptionalAdditionalCostImpl) buybackCost).add(cost);
    }

    public void resetReduceCost() {
        amountToReduceBy = 0;
    }

    // Called by Memory Crystal to reduce mana costs.
    public int reduceCost(int genericManaToReduce) {
        int amountToReduce = genericManaToReduce;
        boolean foundCostToReduce = false;
        for (Object cost : ((Costs) buybackCost)) {
            if (cost instanceof ManaCostsImpl) {
                for (Object c : (ManaCostsImpl) cost) {
                    if (c instanceof GenericManaCost) {
                        int newCostCMC = ((GenericManaCost) c).convertedManaCost() - amountToReduceBy - genericManaToReduce;
                        foundCostToReduce = true;
                        if (newCostCMC > 0) {
                            amountToReduceBy += genericManaToReduce;
                        } else {
                            amountToReduce = ((GenericManaCost) c).convertedManaCost() - amountToReduceBy;
                            amountToReduceBy = ((GenericManaCost) c).convertedManaCost();
                        }
                    }
                }
            }
        }

        if (foundCostToReduce) {
            return amountToReduce;
        }
        return 0;
    }

    @Override
    public boolean isActivated() {
        return buybackCost.isActivated();
    }

    private void resetBuyback(Game game) {
        activateBuyback(game, false);
        resetReduceCost();
        buybackCost.reset();
    }

    private void activateBuyback(Game game, Boolean isActivated) {
        // xmage uses copies, all statuses must be saved to game state, not abilities
        game.getState().setValue(this.getSourceId().toString() + "_activatedBuyback", isActivated);

        // for extra info in cast message
        if (isActivated) {
            buybackCost.activate();
        } else {
            buybackCost.reset();
        }
    }

    public boolean isBuybackActivated(Game game) {
        return Boolean.TRUE.equals(game.getState().getValue(this.getSourceId().toString() + "_activatedBuyback"));
    }

    @Override
    public void addOptionalAdditionalCosts(Ability ability, Game game) {
        if (ability instanceof SpellAbility) {
            Player player = game.getPlayer(ability.getControllerId());
            if (player != null) {
                this.resetBuyback(game);
                // TODO: add AI support to find mana available to pay buyback
                //  canPay checks only single mana available, not total mana usage
                if (player.chooseUse(/*Outcome.Benefit*/ Outcome.AIDontUseIt, "Pay " + buybackCost.getText(false) + " ?", ability, game)) {
                    activateBuyback(game, true);
                    for (Iterator it = ((Costs) buybackCost).iterator(); it.hasNext(); ) {
                        Cost cost = (Cost) it.next();
                        if (cost instanceof ManaCostsImpl) {
                            ability.getManaCostsToPay().add((ManaCostsImpl) cost.copy());
                        } else {
                            ability.getCosts().add(cost.copy());
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getRule() {
        return buybackCost.getText(false) + ' ' + buybackCost.getReminderText();
    }

    @Override
    public String getCastMessageSuffix() {
        return buybackCost.getCastSuffixMessage(0);
    }
}

class SilverstoneRelicBuybackEffect extends ReplacementEffectImpl {

    public SilverstoneRelicBuybackEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Exile);
        staticText = "When {this} resolves and you payed buyback costs, put it back to hand instead";
    }

    public SilverstoneRelicBuybackEffect(final SilverstoneRelicBuybackEffect effect) {
        super(effect);
    }

    @Override
    public SilverstoneRelicBuybackEffect copy() {
        return new SilverstoneRelicBuybackEffect(this);
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.ZONE_CHANGE;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (event.getTargetId().equals(source.getSourceId())) {
            ZoneChangeEvent zEvent = (ZoneChangeEvent) event;
            // if spell fizzled, the sourceId is null
            return zEvent.getFromZone() == Zone.STACK && (zEvent.getToZone() == Zone.GRAVEYARD || zEvent.getToZone() == Zone.BATTLEFIELD)
                    && source.getSourceId().equals(event.getSourceId());
        }
        return false;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        Card card = game.getCard(source.getSourceId());
        if (card != null && source instanceof SilverstoneRelicBuybackAbility) {
            if (((SilverstoneRelicBuybackAbility) source).isBuybackActivated(game)) {
                return card.moveToZone(Zone.HAND, source, game, true, event.getAppliedEffects());
            }
        }
        return false;
    }

}
