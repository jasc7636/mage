
package mage.cards.a;

import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenTargetEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.mana.ColorlessManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.GoldToken;
import mage.players.Player;
import mage.target.common.TargetLandPermanent;
import mage.target.targetpointer.FixedTarget;

import java.util.UUID;

/**
 *
 * @author jasc7636
 */
public final class AbandonedGoldMine extends CardImpl {

    public AbandonedGoldMine(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.LAND},"");

        // {T}: Add {C}.
        this.addAbility(new ColorlessManaAbility());

        // {T}, Sacrifice Strip Mine: Destroy target land.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new DestroyTargetEffect(), new TapSourceCost());
        ability.addCost(new SacrificeSourceCost());
        ability.addTarget(new TargetLandPermanent());
        ability.addEffect(new AbandonedGoldMineEffect());
        this.addAbility(ability);
    }

    public AbandonedGoldMine(final AbandonedGoldMine card) {
        super(card);
    }

    @Override
    public AbandonedGoldMine copy() {
        return new AbandonedGoldMine(this);
    }
}

class AbandonedGoldMineEffect extends OneShotEffect {

    public AbandonedGoldMineEffect() {
        super(Outcome.PutLandInPlay);
        this.staticText = "Its controller creates a colorless Gold artifact token with: \"Sacrifice this artifact: Add one mana of any color.\"";
    }

    public AbandonedGoldMineEffect(final AbandonedGoldMineEffect effect) {
        super(effect);
    }

    @Override
    public AbandonedGoldMineEffect copy() {
        return new AbandonedGoldMineEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = getTargetPointer().getFirstTargetPermanentOrLKI(game, source);
        if (permanent != null) {
            Player player = game.getPlayer(permanent.getControllerId());
            Effect effect = new CreateTokenTargetEffect(new GoldToken());
            effect.setTargetPointer(new FixedTarget(player.getId(), game));
            effect.apply(game, source);
            return true;
        }
        return false;
    }
}