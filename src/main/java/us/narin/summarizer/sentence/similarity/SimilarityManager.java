package us.narin.summarizer.sentence.similarity;

import us.narin.summarizer.set.SetOperator;

import java.util.List;
import java.util.Map;

public class SimilarityManager {
    private Map.Entry<String, List<String>> entrySource;
    private Map.Entry<String, List<String>> entryTarget;

    public SimilarityManager(Map.Entry<String, List<String>> entrySource, Map.Entry<String, List<String>> entryTarget) {
        this.entrySource = entrySource;
        this.entryTarget = entryTarget;
    }

    public float getSimilarity(Similarity similarity){
        switch (similarity){
            case SIMILARITY_COSINE:
                return getCosineSimilarity();
            case SIMILARITY_JACCARD:
                return getJaccardSimilarity();
        }
        return getJaccardSimilarity();
    }

    private float getCosineSimilarity() {
        final List<String> intersection = SetOperator.intersection(entrySource.getValue(), entryTarget.getValue());
        return (float) intersection.size() / (float) (Math.sqrt(entrySource.getValue().size()) * Math.sqrt(entryTarget.getValue().size()));
    }

    private float getJaccardSimilarity() {
        final List<String> intersection = SetOperator.intersection(entrySource.getValue(), entryTarget.getValue());
        final List<String> union = SetOperator.union(entrySource.getValue(), entryTarget.getValue());
        return (float) intersection.size() / (float) union.size();
    }

}
