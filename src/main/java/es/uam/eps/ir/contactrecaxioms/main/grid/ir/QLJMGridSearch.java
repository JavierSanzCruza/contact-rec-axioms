/*
 * Copyright (C) 2020 Information Retrieval Group at Universidad Autónoma
 * de Madrid, http://ir.ii.uam.es and Terrier Team at University of Glasgow,
 * http://terrierteam.dcs.gla.ac.uk/.
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package es.uam.eps.ir.contactrecaxioms.main.grid.ir;

import es.uam.eps.ir.contactrecaxioms.graph.edges.EdgeOrientation;
import es.uam.eps.ir.contactrecaxioms.graph.fast.FastGraph;
import es.uam.eps.ir.contactrecaxioms.main.grid.AlgorithmGridSearch;
import es.uam.eps.ir.contactrecaxioms.main.grid.AlgorithmIdentifiers;
import es.uam.eps.ir.contactrecaxioms.main.grid.Grid;
import es.uam.eps.ir.contactrecaxioms.main.grid.RecommendationAlgorithmFunction;
import es.uam.eps.ir.contactrecaxioms.recommender.ir.QLJM;
import es.uam.eps.ir.ranksys.fast.preference.FastPreferenceData;
import es.uam.eps.ir.ranksys.rec.Recommender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Grid search generator for Query Likelihood algorithm with Jelinek-Mercer smoothing.
 *
 * @see es.uam.eps.ir.contactrecaxioms.recommender.ir.QLJM
 *
 * @param <U> Type of the users.
 *
 * @author Javier Sanz-Cruzado (javier.sanz-cruzado@uam.es)
 * @author Craig Macdonald (craig.macdonald@glasgow.ac.uk)
 * @author Iadh Ounis (iadh.ounis@glasgow.ac.uk)
 * @author Pablo Castells (pablo.castells@uam.es)
 */
public class QLJMGridSearch<U> implements AlgorithmGridSearch<U>
{
    /**
     * Identifier for the trade-off between the regularization term and the original term in
     * the Query Likelihood Jelinek-Mercer formula.
     */
    private static final String LAMBDA = "lambda";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String USEL = "uSel";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String VSEL = "vSel";

    @Override
    public Map<String, Supplier<Recommender<U, U>>> grid(Grid grid, FastGraph<U> graph, FastPreferenceData<U, U> prefData)
    {
        Map<String, Supplier<Recommender<U, U>>> recs = new HashMap<>();
        List<Double> lambdas = grid.getDoubleValues(LAMBDA);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);

        lambdas.forEach(lambda ->
            uSels.forEach(uSel ->
                vSels.forEach(vSel ->
                    recs.put(AlgorithmIdentifiers.QLJM + "_" + uSel + "_" + vSel + "_" + lambda, () -> new QLJM<>(graph, uSel, vSel, lambda)))));

        return recs;
    }

    @Override
    public Map<String, RecommendationAlgorithmFunction<U>> grid(Grid grid)
    {
        Map<String, RecommendationAlgorithmFunction<U>> recs = new HashMap<>();
        List<Double> lambdas = grid.getDoubleValues(LAMBDA);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);

        lambdas.forEach(lambda ->
                uSels.forEach(uSel ->
                        vSels.forEach(vSel ->
                                recs.put(AlgorithmIdentifiers.QLJM + "_" + uSel + "_" + vSel + "_" + lambda, (graph, prefData) -> new QLJM<>(graph, uSel, vSel, lambda)))));

        return recs;
    }

}
