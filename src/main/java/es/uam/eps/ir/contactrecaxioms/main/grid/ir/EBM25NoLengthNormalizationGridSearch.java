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
import es.uam.eps.ir.contactrecaxioms.main.grid.Grid;
import es.uam.eps.ir.contactrecaxioms.main.grid.RecommendationAlgorithmFunction;
import es.uam.eps.ir.contactrecaxioms.recommender.ir.EBM25NoLengthNormalization;
import es.uam.eps.ir.ranksys.fast.preference.FastPreferenceData;
import es.uam.eps.ir.ranksys.rec.Recommender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static es.uam.eps.ir.contactrecaxioms.main.grid.AlgorithmIdentifiers.EBM25NOLEN;

/**
 * Grid search generator for the Extreme BM25 algorithm (without length normalization).
 *
 * @see es.uam.eps.ir.contactrecaxioms.recommender.ir.EBM25NoLengthNormalization
 *
 * @param <U> Type of the users.
 *
 * @author Javier Sanz-Cruzado (javier.sanz-cruzado@uam.es)
 * @author Craig Macdonald (craig.macdonald@glasgow.ac.uk)
 * @author Iadh Ounis (iadh.ounis@glasgow.ac.uk)
 * @author Pablo Castells (pablo.castells@uam.es)
 */
public class EBM25NoLengthNormalizationGridSearch<U> implements AlgorithmGridSearch<U>
{

    /**
     * Identifier for parameter b
     */
    private static final String B = "b";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String USEL = "uSel";
    /**
     * Identifier for the orientation of the target user neighborhood
     */
    private static final String VSEL = "vSel";
    /**
     * Identifier for the orientation for the document length
     */
    private static final String DLSEL = "dlSel";

    @Override
    public Map<String, Supplier<Recommender<U, U>>> grid(Grid grid, FastGraph<U> graph, FastPreferenceData<U, U> prefData)
    {
        Map<String, Supplier<Recommender<U, U>>> recs = new HashMap<>();
        List<Double> bs = grid.getDoubleValues(B);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);
        List<EdgeOrientation> dlSels = grid.getOrientationValues(DLSEL);

        bs.forEach(b ->
            uSels.forEach(uSel ->
                vSels.forEach(vSel ->
                    dlSels.forEach(dlSel ->
                        recs.put(EBM25NOLEN + "_" + uSel + "_" + vSel + "_" + dlSel + "_" + b, () -> new EBM25NoLengthNormalization<>(graph, uSel, vSel, dlSel))))));

        return recs;
    }

    @Override
    public Map<String, RecommendationAlgorithmFunction<U>> grid(Grid grid)
    {
        Map<String, RecommendationAlgorithmFunction<U>> recs = new HashMap<>();
        List<Double> bs = grid.getDoubleValues(B);
        List<EdgeOrientation> uSels = grid.getOrientationValues(USEL);
        List<EdgeOrientation> vSels = grid.getOrientationValues(VSEL);
        List<EdgeOrientation> dlSels = grid.getOrientationValues(DLSEL);

        bs.forEach(b ->
            uSels.forEach(uSel ->
                vSels.forEach(vSel ->
                    dlSels.forEach(dlSel ->
                        recs.put(EBM25NOLEN + "_" + uSel + "_" + vSel + "_" + dlSel + "_" + b, (graph, prefData) -> new EBM25NoLengthNormalization<>(graph, uSel, vSel, dlSel))))));

        return recs;
    }

}
