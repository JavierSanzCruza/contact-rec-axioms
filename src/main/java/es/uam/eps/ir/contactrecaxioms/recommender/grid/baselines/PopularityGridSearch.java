/*
 *  Copyright (C) 2016 Information Retrieval Group at Universidad Aut�noma
 *  de Madrid, http://ir.ii.uam.es
 * 
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package es.uam.eps.ir.contactrecaxioms.recommender.grid.baselines;

import es.uam.eps.ir.contactrecaxioms.graph.edges.EdgeOrientation;
import es.uam.eps.ir.contactrecaxioms.graph.fast.FastGraph;
import es.uam.eps.ir.contactrecaxioms.recommender.basic.Popularity;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.AlgorithmGridSearch;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.Grid;
import es.uam.eps.ir.contactrecaxioms.recommender.grid.RecommendationAlgorithmFunction;
import es.uam.eps.ir.ranksys.fast.preference.FastPreferenceData;
import es.uam.eps.ir.ranksys.rec.Recommender;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static es.uam.eps.ir.contactrecaxioms.recommender.grid.AlgorithmIdentifiers.POP;

/**
 * Grid search generator for Popularity algorithm.
 * @author Javier Sanz-Cruzado Puig
 * @param <U> Type of the users.
 */
public class PopularityGridSearch<U> implements AlgorithmGridSearch<U>
{       
    @Override
    public Map<String, RecommendationAlgorithmFunction<U>> grid(Grid grid)
    {
        Map<String, RecommendationAlgorithmFunction<U>> recs = new HashMap<>();
        
        RecommendationAlgorithmFunction<U> bifunction = (graph, prefdata) -> 
        {
            return new Popularity<>(graph, EdgeOrientation.IN);
        };
        
        recs.put(POP, bifunction);
        return recs;
    }

    @Override
    public Map<String, Supplier<Recommender<U, U>>> grid(Grid grid, FastGraph<U> graph, FastPreferenceData<U, U> prefData)
    {
        Map<String, Supplier<Recommender<U,U>>> recs = new HashMap<>();

        recs.put(POP, () ->
        {
           return new Popularity<>(graph, EdgeOrientation.IN);
        });
        
        return recs;
    }   
}