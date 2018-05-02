package us.narin.summarizer.sentence;

import kr.bydelta.koala.data.Morpheme;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.Word;
import kr.bydelta.koala.hnn.SentenceSplitter;
import kr.bydelta.koala.hnn.Tagger;
import scala.collection.Iterator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by endlessdev on 8/23/17.
 */
public class SentenceSource {
    private String article;

    public SentenceSource(String article) {
        this.article = article;
    }

    public List<String> getSentences() {
        final SentenceSplitter sentenceSplitter = new SentenceSplitter();
        return sentenceSplitter.jSentences(this.article)
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public Map<String, List<String>> getExtractedSentences() {
        final Tagger tagger = new Tagger();
        final Map<String, List<String>> parsedSentence = new LinkedHashMap<>();

        for (String sentence : getSentences()) {
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
        return parsedSentence;
    }

}
