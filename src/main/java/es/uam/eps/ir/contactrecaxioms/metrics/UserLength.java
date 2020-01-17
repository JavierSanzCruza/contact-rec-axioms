package es.uam.eps.ir.contactrecaxioms.metrics;

import es.uam.eps.ir.ranksys.metrics.rank.RankingDiscountModel;
import es.uam.eps.ir.ranksys.metrics.rel.RelevanceModel;
import es.uam.eps.ir.ranksys.novdiv.itemnovelty.metrics.ItemNoveltyMetric;

public class UserLength<U> extends ItemNoveltyMetric<U,U>
{
    /**
     * Constructor.
     *
     * @param cutoff maximum size of the recommendation list that is evaluated
     * @param novelty novelty model
     * @param relModel relevance model
     * @param disc ranking discount model
     */
    public UserLength(int cutoff, UserLengthNovelty<U> novelty, RelevanceModel<U, U> relModel, RankingDiscountModel disc) {
        super(cutoff, novelty, relModel, disc);
    }
}
