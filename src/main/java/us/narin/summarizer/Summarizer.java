package us.narin.summarizer;

import us.narin.summarizer.graph.GraphBuilder;
import us.narin.summarizer.sentence.SentenceSource;
import us.narin.summarizer.sentence.ranker.SentenceRanker;

import java.util.*;
import java.util.stream.Collectors;

public class Summarizer {

    private String content;

    public Summarizer(String content) {
        this.content = content;
    }

    public List<String> summarize() {
        final SentenceSource sentenceSource = new SentenceSource(content);

        final List<String> sentences = sentenceSource.getSentences();
        final Map<String, List<String>> extractedSentences = sentenceSource.getExtractedSentences();

        final GraphBuilder graphBuilder = new GraphBuilder(extractedSentences);

        return new SentenceRanker(sentences, graphBuilder.build()).getRankedSentences()
                .stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }
}


