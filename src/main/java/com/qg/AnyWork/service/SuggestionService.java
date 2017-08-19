package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.SuggestionDao;
import com.qg.AnyWork.dto.RequestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by logan on 2017/8/18.
 */
@Service
public class SuggestionService {
    @Autowired
    private SuggestionDao suggestionDao;

    public RequestResult addSuggestion(int userId,String suggestion){
        if (suggestion==null||suggestion.equals("")) return new RequestResult(0,"请填写你的建议");
        suggestionDao.addSuggestion(userId,suggestion);
        return new RequestResult(1,"成功");
    }
}
