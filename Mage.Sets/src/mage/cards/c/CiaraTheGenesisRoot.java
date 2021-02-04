package mage.cards.c;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.mana.BasicManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledLandPermanent;
import mage.filter.predicate.permanent.AnotherPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author AsterAether
 */
public final class CiaraTheGenesisRoot extends CardImpl {

    public CiaraTheGenesisRoot(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{G}");

        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.DRUID);
        this.power = new MageInt(0);
        this.toughness = new MageInt(1);

        // Ciara, the Genesis Root has all activated abilities of all lands you control.
        this.addAbility(new SimpleStaticAbility(new CiaraTheGenesisRootGainAbilitiesEffect()));
    }

    private CiaraTheGenesisRoot(final CiaraTheGenesisRoot card) {
        super(card);
    }

    @Override
    public CiaraTheGenesisRoot copy() {
        return new CiaraTheGenesisRoot(this);
    }
}

class CiaraTheGenesisRootGainAbilitiesEffect extends ContinuousEffectImpl {

    private static final FilterPermanent filter = new FilterControlledLandPermanent();

    static {
        filter.add(AnotherPredicate.instance);
    }

    CiaraTheGenesisRootGainAbilitiesEffect() {
        super(Duration.WhileOnBattlefield, Layer.AbilityAddingRemovingEffects_6, SubLayer.NA, Outcome.AddAbility);
        staticText = "{this} has all activated abilities of all lands you control.";
        this.addDependencyType(DependencyType.AddingAbility);
    }

    private CiaraTheGenesisRootGainAbilitiesEffect(final CiaraTheGenesisRootGainAbilitiesEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent perm = game.getPermanent(source.getSourceId());
        if (perm == null) {
            return true;
        }
        for (Ability ability : game.getState()
                .getBattlefield()
                .getActivePermanents(filter, source.getControllerId(), source.getSourceId(), game)
                .stream()
                .map(permanent -> permanent.getAbilities(game))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(ability -> ability.getAbilityType() == AbilityType.ACTIVATED
                        || ability.getAbilityType() == AbilityType.MANA)
                .collect(Collectors.toList())) {
            if (!(ability instanceof BasicManaAbility)
                    || perm.getAbilities(game)
                    .stream()
                    .noneMatch(ability.getClass()::isInstance)) {
                perm.addAbility(ability, source.getSourceId(), game);
            }
        }
        return true;
    }

    @Override
    public CiaraTheGenesisRootGainAbilitiesEffect copy() {
        return new CiaraTheGenesisRootGainAbilitiesEffect(this);
    }
}