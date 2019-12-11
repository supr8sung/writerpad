package com.xebia.fs101.writerpad.service;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlagiarismFinderService {
    private double plagiarismFactor;

    PlagiarismFinderService(@Value("${plagiarism.factor}") double plagiarismFactor) {

        this.plagiarismFactor = plagiarismFactor;
    }

    private SimilarityStrategy strategy;

    public boolean isPlagiarism(String source, List<String> target) {

        strategy = new JaroWinklerStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        return target.parallelStream().anyMatch(
                e -> service.score(source, e) > plagiarismFactor);
    }
}
