/*
 *  Copyright (C) 2020 Information Retrieval Group at Universidad Autónoma
 *  de Madrid, http://ir.ii.uam.es
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package es.uam.eps.ir.contactrecaxioms.recommender.ir;

import es.uam.eps.ir.contactrecaxioms.graph.edges.EdgeOrientation;
import es.uam.eps.ir.contactrecaxioms.graph.fast.FastGraph;

/**
 * Adaptation of the pivoted normalization vector space model (VSM), without length normalization.
 *
 * @author Javier Sanz-Cruzado (javier.sanz-cruzado@uam.es)
 * @author Pablo Castells (pablo.castells@uam.es)
 *
 * @param <U> Type of the users.
 */
public class PivotedNormalizationVSMNoLengthNormalization<U> extends PivotedNormalizationVSM<U>
{
    /**
     * Constructor.
     * @param graph the training network.
     * @param uSel neighborhood orientation selected for the target user.
     * @param vSel neighborhood orientation selected for the candidate user.
     */
    public PivotedNormalizationVSMNoLengthNormalization(FastGraph<U> graph, EdgeOrientation uSel, EdgeOrientation vSel)
    {
        super(graph, uSel, vSel, 0.0);
    }
}
