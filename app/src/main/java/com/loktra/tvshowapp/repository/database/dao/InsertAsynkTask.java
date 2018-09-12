package com.loktra.tvshowapp.repository.database.dao;

import android.os.AsyncTask;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.List;

public class InsertAsynkTask extends AsyncTask<List<TVShowResponse>,Void,Void> {
    private TVShowDao mAsynkTaskDao;

    public InsertAsynkTask(TVShowDao mWordDao) {
        this.mAsynkTaskDao = mWordDao;
    }

    @Override
    protected final Void doInBackground(List<TVShowResponse>... tvShowResponseList) {
        List<TVShowResponse> result = tvShowResponseList[0];
        mAsynkTaskDao.insertAll(result);
        return null;
    }
}
