package us.narin.summarizer;

import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.hnn.SentenceSplitter;
import kr.bydelta.koala.hnn.Tagger;
import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import scala.collection.Iterator;
import us.narin.summarizer.utils.ListUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Summarizer {

    public static void main(String[] args) throws FileNotFoundException {

        Tagger tagger = new Tagger();
        SentenceSplitter sentenceSplitter = new SentenceSplitter();

        String content = new Scanner(new File("./test.txt")).useDelimiter("\\Z").next();

        List<String> splitSentenceList = sentenceSplitter
                .jSentences(content)
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());

        Map<String, List<String>> parsedSentence = new LinkedHashMap<>();

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph =
                new SimpleWeightedGraph<>
                        (DefaultWeightedEdge.class);

        for (String sentence : splitSentenceList) {
            Sentence analyzedSentence = tagger.tagSentence(sentence);
            Iterator iterator = analyzedSentence.words().iterator();
            List<String> detectedNouns = new ArrayList<>();

            while (iterator.hasNext()) {
                Word word = (Word) iterator.next();
                Iterator wordIterator = word.iterator();

                while (wordIterator.hasNext()) {
                    Morpheme morpheme = (Morpheme) wordIterator.next();
                    if (morpheme.isNoun()) {
                        String plainWord = morpheme.toString().split("/")[0];
                        detectedNouns.add(plainWord);
                    }
                }
            }
            parsedSentence.put(sentence, detectedNouns);
        }

        splitSentenceList.forEach(graph::addVertex);

        for (Map.Entry<String, List<String>> entrySource : parsedSentence.entrySet()) {
            for (Map.Entry<String, List<String>> entryTarget : parsedSentence.entrySet()) {
                if (!Objects.equals(entrySource.getKey(), entryTarget.getKey())) {

                    float similarity = getSimilarity(entrySource, entryTarget);

                    if (similarity > 0 && graph.getEdge(entrySource.getKey(), entryTarget.getKey()) == null) {
                        DefaultWeightedEdge e = graph.addEdge(entrySource.getKey(), entryTarget.getKey());
                        graph.setEdgeWeight(e, similarity);
                    }
                }
            }
        }

        VertexScoringAlgorithm<String, Double> pageRank = new PageRank<>(graph);
        List<Map.Entry<String, Double>> resultSentences = pageRank.getScores().entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getValue() < o2.getValue() ? 1 : -1)
                .limit(3)
                .collect(Collectors.toList())
                .stream()
                .sorted((source, target) -> {
                    int sourceIdx = 0;
                    int targetIdx = 0;
                    for (int i = 0; i < splitSentenceList.size(); i++) {
                        String sentence = splitSentenceList.get(i);
                        if (Objects.equals(sentence, source.getKey()))
                            sourceIdx = i;
                        else if (Objects.equals(sentence, target.getKey()))
                            targetIdx = i;
                    }
                    return sourceIdx > targetIdx ? 1 : -1;
                }).collect(Collectors.toList());


        resultSentences.forEach(i -> System.out.print(i.getKey()));
    }

    private static float getSimilarity(Map.Entry<String, List<String>> entrySource, Map.Entry<String, List<String>> entryTarget) {
        List<String> intersection = ListUtils.intersection(entrySource.getValue(), entryTarget.getValue());
        return (float) intersection.size() / (float) (Math.sqrt(entrySource.getValue().size()) * Math.sqrt(entryTarget.getValue().size()));
    }

}


