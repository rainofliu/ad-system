package com.ajin.ad.search;

import com.ajin.ad.search.vo.SearchRequest;
import com.ajin.ad.search.vo.SearchResponse;

/**
 * @Author: ajin
 * @Date: 2019/4/24 21:15
 */
public interface ISearch {


    SearchResponse fetchAds(SearchRequest request);
}
