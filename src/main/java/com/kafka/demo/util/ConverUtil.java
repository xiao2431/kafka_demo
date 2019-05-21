package com.kafka.demo.util;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConverUtil {
    public static <T> List<List<T>> convert2BatchList(List<T> list, Integer batchSize) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }

        Integer limit = list.size() < batchSize ? 1 : (list.size() + 1) / batchSize;
        List<List<T>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> {
            return list.stream().skip(a * batchSize).limit(batchSize).parallel().collect(Collectors.toList());
        })
                .collect(Collectors.toList());
        return splitList;
    }
}
