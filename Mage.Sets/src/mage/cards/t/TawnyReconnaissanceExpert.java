package mage.cards.t;

import mage.abilities.LoyaltyAbility;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.PlaneswalkerEntersWithLoyaltyCountersAbility;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.keyword.InvestigateEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.DamagedPlayerEvent;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.BirdToken;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jasc7636
 */
public final class TawnyReconnaissanceExpert extends CardImpl {

    public TawnyReconnaissanceExpert(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.PLANESWALKER}, "{2}{W}");

        this.addSuperType(SuperType.LEGENDARY);
        //this.subtype.add(SubType.TWANY);
        this.addAbility(new PlaneswalkerEntersWithLoyaltyCountersAbility(3));

        // Whenever one or more creatures you control with flying deal combat damage to a player, investigate.
        this.addAbility(new TawnyReconnaissanceExpertTriggeredAbility());

        // -1: Create a 1/1 white Bird creature token with flying.
        this.addAbility(new LoyaltyAbility(new CreateTokenEffect(new BirdToken()), -1));
    }

    private TawnyReconnaissanceExpert(final TawnyReconnaissanceExpert card) {
        super(card);
    }

    @Override
    public TawnyReconnaissanceExpert copy() {
        return new TawnyReconnaissanceExpert(this);
    }
}

class TawnyReconnaissanceExpertTriggeredAbility extends TriggeredAbilityImpl {

    private final List<UUID> damagedPlayerIds = new ArrayList<>();

    TawnyReconnaissanceExpertTriggeredAbility() {
        super(Zone.BATTLEFIELD, new InvestigateEffect(), false);
    }

    private TawnyReconnaissanceExpertTriggeredAbility(final TawnyReconnaissanceExpertTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public TawnyReconnaissanceExpertTriggeredAbility copy() {
        return new TawnyReconnaissanceExpertTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.DAMAGED_PLAYER
                || event.getType() == GameEvent.EventType.COMBAT_DAMAGE_STEP_POST;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.DAMAGED_PLAYER) {
            if (((DamagedPlayerEvent) event).isCombatDamage()) {
                Permanent creature = game.getPermanent(event.getSourceId());
                if (creature != null && creature.isControlledBy(controllerId)
                        && creature.hasAbility(FlyingAbility.getInstance(), game) && !damagedPlayerIds.contains(event.getTargetId())) {
                    damagedPlayerIds.add(event.getTargetId());
                    return true;
                }
            }
        }
        if (event.getType() == GameEvent.EventType.COMBAT_DAMAGE_STEP_POST) {
            damagedPlayerIds.clear();
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever one or more creatures you control with flying deal combat damage to a player, investigate";
    }
}
