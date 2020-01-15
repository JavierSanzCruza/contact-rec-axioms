/*
 *  Copyright (C) 2016 Information Retrieval Group at Universidad Aut�noma
 *  de Madrid, http://ir.ii.uam.es
 * 
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package es.uam.eps.ir.contactrecaxioms.recommender.grid.ir;

import es.uam.eps.ir.contactrecaxioms.graph.edges.EdgeOrientation;
import es.uam.eps.ir.contactrecaxioms.graph.fast.FastGraph;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.AlgorithmGridSearch;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.AlgorithmIdentifiers;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.Grid;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.RecommendationAlgorithmFunction;
import es.uam.eps.ir.contactrecaxioms.recommender.ir.QLL;
import es.uam.eps.ir.ranksys.fast.preference.FastPreferenceData;
import es.uam.eps.ir.ranksys.rec.Recommender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Grid search generator for Query Likelihood algorithm with Laplace normalization.
 * @author Javier Sanz-Cruzado Puig
 * @param <U> Type of the users.
 */
public class QLLGridSearch<U> implements AlgorithmGridSearch<U>
{
    /**
     * Identifier for the trade-off between the regularization term and the original term in
     * the Query Likelihood Laplace formula.
     */
    private static final String PHI  = "phi";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String USEL = "uSel";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String VSEL = "vSel";
    
    @Override
    public Map<String, Supplier<Recommender<U, U>>> grid(Grid grid, FastGraph<U> graph, FastPreferenceData<U,U> prefData)
    {
        Map<String, Supplier<Recommender<U,U>>> recs = new HashMap<>();
        List<Double> phis = grid.getDoubleValues(PHI);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);
        
        phis.stream().forEach(phi -> 
        {
            uSels.stream().forEach(uSel -> 
            {
                vSels.stream().forEach(vSel -> 
                {
                    recs.put(AlgorithmIdentifiers.QLL + "_" + uSel + "_" + vSel + "_" + phi, () ->
                    {
                       return new QLL<>(graph, uSel, vSel, phi);
                    });
                });
            });
        });
        return recs;
    }

    @Override
    public Map<String, RecommendationAlgorithmFunction<U>> grid(Grid grid)
    {
        Map<String, RecommendationAlgorithmFunction<U>> recs = new HashMap<>();
        List<Double> phis = grid.getDoubleValues(PHI);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);
        
        phis.stream().forEach(phi -> 
        {
            uSels.stream().forEach(uSel -> 
            {
                vSels.stream().forEach(vSel -> 
                {
                    recs.put(AlgorithmIdentifiers.QLL + "_" + uSel + "_" + vSel + "_" + phi, (graph, prefData) ->
                    {
                       return new QLL<>(graph, uSel, vSel, phi);
                    });
                });
            });
        });
        return recs;
    }
    
}
