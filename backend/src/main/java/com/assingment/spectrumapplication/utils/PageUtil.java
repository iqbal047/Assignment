package com.assingment.spectrumapplication.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class PageUtil {

    public static Pageable getPageable(Integer pageNumber, Integer pageSize, String sortDirection, String sortCriteria) {
        int page = pageNumber == null ? 0 : pageNumber;
        int size = pageSize == null ? Integer.MAX_VALUE : pageSize;
        String[] sortColumns = {"id"};
        if (StringUtils.hasText(sortCriteria)) {
            sortColumns = sortCriteria.split(" *, *");
        }
        if (StringUtils.hasText(sortDirection)) {
            return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortColumns));
        } else {
            return PageRequest.of(page, size);
        }
    }
}
