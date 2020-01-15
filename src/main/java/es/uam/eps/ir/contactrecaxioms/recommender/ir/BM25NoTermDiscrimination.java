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
import es.uam.eps.ir.contactrecaxioms.recommender.UserFastRankingRecommender;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;

/**
 * Adaptation of the BM-25 Information Retrieval Algorithm for user recommendation, without term discrimination.
 * 
 * Sparck Jones, K., Walker, S., Roberton S.E. A Probabilistic Model of Information Retrieval: Development and Comparative Experiments. 
 * Information Processing and Management 36. February 2000, pp. 779-808 (part 1), pp. 809-840 (part 2).
 * 
 * @author Javier Sanz-Cruzado Puig
 * @param <U> type of the users
 */
public class BM25NoTermDiscrimination<U> extends UserFastRankingRecommender<U>
{
    /**
     * Parameter that tunes the effect of the neighborhood size. Between 0 and 1
     */
    private final double b;
    /**
     * Parameter that tunes the effect of the term frequency on the formula.
     */
    private final double k;
    /**
     * Neighborhood selection for the target users.
     */
    private final EdgeOrientation uSel;
        /**
     * Neighborhood selection for the candidate users.
     */
    private final EdgeOrientation vSel;
    /**
     * Neighbour selection for the document length
     */
    private final EdgeOrientation dlSel;
    /**
     * Average size of the neighborhood of the candidate nodes.
     */
    private double avgSize;
    /**
     * Number of users in the network.
     */
    private long numUsers;
    /**
     * Neighborhood sizes for each user.
     */
    private final Int2DoubleOpenHashMap size;

    private final Int2DoubleOpenHashMap wLengths;
    /**
     * Constructor.
     * @param graph Graph
     * @param uSel Selection of the neighbours of the target user
     * @param vSel Selection of the neighbours of the candidate user
     * @param dlSel Selection of the neighbours for the document length
     * @param b Tunes the effect of the neighborhood size. Between 0 and 1.
     * @param k parameter of the algorithm.
     */
    public BM25NoTermDiscrimination(FastGraph<U> graph, EdgeOrientation uSel, EdgeOrientation vSel, EdgeOrientation dlSel, double b, double k)
    {
        super(graph);
        
        this.dlSel = dlSel;
        this.b = b;
        this.k = k;
        this.size = new Int2DoubleOpenHashMap();
        this.wLengths = new Int2DoubleOpenHashMap();
        this.numUsers = graph.getVertexCount();
        
        this.uSel = uSel;
        this.vSel = vSel.invertSelection();
        this.avgSize = this.getAllUidx().mapToDouble(vidx -> 
        {
            // Compute RSJ
            double rsjV = graph.getNeighborhood(vidx, this.vSel).count();
            this.wLengths.put(vidx, rsjV);

            // Compute size
            double val = graph.getNeighborhoodWeights(vidx, dlSel).mapToDouble(widx -> widx.v2).sum();

            this.size.put(vidx, val);
            return val;
        }).average().getAsDouble();
    }

    @Override
    public Int2DoubleMap getScoresMap(int uidx) 
    {
        Int2DoubleOpenHashMap scoresMap = new Int2DoubleOpenHashMap();
        scoresMap.defaultReturnValue(0.0);

        
        if(Double.isFinite(this.k))
        {
            graph.getNeighborhood(uidx, uSel).forEach(widx -> 
            {
                graph.getNeighborhoodWeights(widx, vSel).forEach(vidx ->
                {
                    double weight = vidx.v2;
                    double s = this.size.get(vidx.v1);
                    
                    double num = (this.k + 1.0)*weight;
                    double den = this.k*(1-b + (b*s/avgSize)) + weight;
                    
                    scoresMap.addTo(vidx.v1, num/den);
                });
            });
        }
        else
        {
            graph.getNeighborhood(uidx, uSel).forEach(widx -> 
            {
                graph.getNeighborhoodWeights(widx, vSel).forEach(vidx ->
                {
                    double weight = vidx.v2;
                    double s = this.size.get(vidx.v1);
                    
                    double num = weight;
                    double den = (1-b + (b*s/avgSize));
                    
                    scoresMap.addTo(vidx.v1, num/den);
                });
            });
        }

        return scoresMap;
    }
}