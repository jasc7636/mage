package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 * @author jasc7636
 */
public final class CalebGannonCube extends ExpansionSet {

    private static final CalebGannonCube instance = new CalebGannonCube();

    public static CalebGannonCube getInstance() {
        return instance;
    }

    private CalebGannonCube() {
        super("Caleb Gannon Cube", "CGC", ExpansionSet.buildDate(2021, 1, 7), SetType.CUSTOM_SET);
        this.blockName = "Caleb Gannon Cube";
        this.hasBasicLands = false;

        cards.add(new SetCardInfo("Formless", 1, Rarity.RARE, mage.cards.f.Formless.class));
        cards.add(new SetCardInfo("Gentle Gardener", 2, Rarity.UNCOMMON, mage.cards.g.GentleGardener.class));
        cards.add(new SetCardInfo("Time Fracture", 3, Rarity.MYTHIC, mage.cards.t.TimeFracture.class));
        cards.add(new SetCardInfo("Mox Locus", 4, Rarity.MYTHIC, mage.cards.m.MoxLocus.class));
        cards.add(new SetCardInfo("Black Locus", 5, Rarity.MYTHIC, mage.cards.b.BlackLocus.class));
        cards.add(new SetCardInfo("One With The Universe", 6, Rarity.MYTHIC, mage.cards.o.OneWithTheUniverse.class));
        cards.add(new SetCardInfo("Ciara, the Genesis Root", 7, Rarity.RARE, mage.cards.c.CiaraTheGenesisRoot.class));
        cards.add(new SetCardInfo("Abandoned Gold Mine", 8, Rarity.RARE, mage.cards.a.AbandonedGoldMine.class));
        cards.add(new SetCardInfo("Civilization's Reach", 9, Rarity.UNCOMMON, mage.cards.c.CivilizationsReach.class));
        cards.add(new SetCardInfo("Mirror-Legion Militant", 10, Rarity.UNCOMMON, mage.cards.m.MirrorLegionMilitant.class));
        cards.add(new SetCardInfo("Shiv's Core", 11, Rarity.RARE, mage.cards.s.ShivsCore.class));
        cards.add(new SetCardInfo("Pull of the Wilderness", 12, Rarity.UNCOMMON, mage.cards.p.PullOfTheWilderness.class));
        cards.add(new SetCardInfo("Extend Reality", 13, Rarity.RARE, mage.cards.e.ExtendReality.class));
        cards.add(new SetCardInfo("Silverstone Relic", 14, Rarity.UNCOMMON, mage.cards.s.SilverstoneRelic.class));
        cards.add(new SetCardInfo("Bounty of the Grove", 15, Rarity.UNCOMMON, mage.cards.b.BountyOfTheGrove.class));
        cards.add(new SetCardInfo("Time Gambler", 16, Rarity.UNCOMMON, mage.cards.t.TimeGambler.class));
        cards.add(new SetCardInfo("Vronos, Etherium Inquisitor", 17, Rarity.RARE, mage.cards.v.VronosEtheriumInquisitor.class));
        cards.add(new SetCardInfo("Reversal of Fortunes", 18, Rarity.MYTHIC, mage.cards.r.ReversalOfFortunes.class));
        cards.add(new SetCardInfo("Tawny, Reconnaissance Expert", 19, Rarity.COMMON, mage.cards.t.TawnyReconnaissanceExpert.class));
    }
}
