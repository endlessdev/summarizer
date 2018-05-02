package us.narin.summarizer.sentence.ranker;

import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SentenceRanker {
    private List<String> sentences;
    private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;

    public SentenceRanker(List<String> sentences, SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        this.sentences = sentences;
        this.graph = graph;
    }

    public List<Map.Entry<String, Double>> getRankedSentences() {
        VertexScoringAlgorithm<String, Double> pageRank = new PageRank<>(graph);
        return pageRank.getScores().entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getValue() < o2.getValue() ? 1 : -1)
                .limit(3)
                .collect(Collectors.toList())
                .stream()
                .sorted((source, target) ->
                        sentences.indexOf(source.getKey()) > sentences.indexOf(target.getKey()) ? 1 : -1)
                .collect(Collectors.toList());
    }
}
