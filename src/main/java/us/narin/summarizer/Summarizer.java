package us.narin.summarizer;

import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.twt.Tagger;
import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import scala.collection.Iterator;
import us.narin.summarizer.utils.ListUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Summarizer {

    public static void main(String[] args) throws FileNotFoundException {

        Tagger tagger = new Tagger();
        String content = new Scanner(new File("./test.txt")).useDelimiter("\\Z").next();
        List<String> keywords = Arrays.asList("북한", "미사일");
        String[] splitSentences = content.split("[.?!\n]");
        Map<String, List<String>> parsedSentence = new LinkedHashMap<>();

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph =
                new SimpleDirectedWeightedGraph<>
                        (DefaultWeightedEdge.class);

        System.out.println(Arrays.toString(splitSentences));

        for (String sentence : splitSentences) {
            sentence = sentence.trim();
            System.out.println(sentence);
            Sentence analyzedSentence = tagger.tagSentence(sentence);
            Iterator iterator = analyzedSentence.words().iterator();
            List<String> detectedNouns = new ArrayList<>();

            while (iterator.hasNext()) {
                Word word = (Word) iterator.next();
                Iterator wordIterator = word.iterator();

                while (wordIterator.hasNext()) {
                    Morpheme morpheme = (Morpheme) wordIterator.next();
//                    if (morpheme.isNoun() || morpheme.isPredicate()) {
                    if (morpheme.isNoun()) {
//                        WTF - KoalaNLP의 Morpheme 클래스에서 순수한 단어만 가져오는 함수가 없다.
                        String plainWord = morpheme.toString().split("/")[0];
                        System.out.println(morpheme);
                        detectedNouns.add(plainWord);
                    }
                }
            }
            parsedSentence.put(sentence, detectedNouns);
        }

        System.out.println(parsedSentence);


        for (Map.Entry<String, List<String>> entry : parsedSentence.entrySet()) {
            String key = entry.getKey();
            graph.addVertex(key);
            List<String> value = entry.getValue();

            System.out.println(key);
            System.out.println(value);

        }

        parsedSentence.entrySet().stream().sorted((o1, o2) -> {
            System.out.println("=== COMPARING START ===");
            List<String> intersection = ListUtils.intersection(o1.getValue(), o2.getValue());
            List<String> union = ListUtils.union(o1.getValue(), o2.getValue());

            float similarity = (float) intersection.size() / (float) union.size();
            System.out.println(String.format("'%s'와 '%s'의 유사도는 %f", o1.getKey(), o2.getKey(), similarity));
            System.out.println("=== COMPARING END ===");

            if (similarity > 0) {
                DefaultWeightedEdge e = graph.addEdge(o1.getKey(), o2.getKey());
                graph.setEdgeWeight(e, similarity);
            }

            return 0;
        }).forEach(System.out::println);
        System.out.println(graph);

        VertexScoringAlgorithm<String, Double> pr = new PageRank<>(graph);

        pr.getScores().entrySet().stream()
                .map(entity -> {
//                  TODO 검색 키워드가 포함된 문장의 경우 페이지 랭크의 값을 더해줘야 함.
                    List<String> mutualKeywords = ListUtils.intersection(parsedSentence.get(entity.getKey()), keywords);
                    if (!mutualKeywords.isEmpty()) {
                        System.out.println((Double) (entity.getValue() + (entity.getValue() + mutualKeywords.size() / 100)));
                    }
//                    entity.setValue((Double) (entity.getValue() + (entity.getValue() + mutalKeywords.size() / 100)));
                    return entity;
                })
                .sorted((o1, o2) -> o1.getValue() < o2.getValue() ? 1 : -1)
                .forEach(System.out::println);

    }
}


