package mage.cards.t;

import mage.MageInt;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.effects.common.turn.AddExtraTurnControllerEffect;
import mage.cards.AdventureCard;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;

import java.util.UUID;

/**
 * @author TheElk801
 */
public final class TimeGambler extends AdventureCard {

    public TimeGambler(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, new CardType[]{CardType.INSTANT}, "{4}{U}{U}", "Time Gambit", "{R}{R}");

        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.WIZARD);
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // When Time Gambler enters the battlefield, take an extra turn after this one.
        this.addAbility(new EntersBattlefieldAbility(new AddExtraTurnControllerEffect()));

        // Time Gambit
        // Take an extra turn after this one. At the beginning of that turn's end step, you lose the game.
        this.getSpellCard().getSpellAbility().addEffect(new AddExtraTurnControllerEffect(true));
    }

    private TimeGambler(final TimeGambler card) {
        super(card);
    }

    @Override
    public TimeGambler copy() {
        return new TimeGambler(this);
    }
}
