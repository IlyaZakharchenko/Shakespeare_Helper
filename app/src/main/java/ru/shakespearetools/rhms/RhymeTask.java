package ru.shakespearetools.rhms;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by al on 14.07.17.
 */



public class RhymeTask extends AsyncTask<String, Void, ArrayList<String>> {
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        Log.e("Alm", "startDownloading");

        ArrayList<String> answer = new ArrayList<>();

        String word = params[0];

        String str = "{}";

        URL url = null;
        try {
            url = new URL("http://rhymebrain.com/talk?function=getRhymes&word=" + word);

            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int bytesRead = 0;
            while ((bytesRead = bis.read(buffer)) > 0) {
                String text = new String(buffer, 0, bytesRead);
                sb.append(text);
            }

            str = sb.toString();

            //Log.e("Alm", str);

            bis.close();

        } catch (MalformedURLException e) {
            Log.e("Alm", "errorDownloading");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Alm", "errorDownloading");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Parsing
        try {
            JSONArray array = new JSONArray(str);

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String oneWord = jsonObject.getString("word");
                String flags = jsonObject.getString("flags");
                if(flags.contains("b")&&!flags.contains("a")){
                    answer.add(oneWord);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("Alm", "endDownloading");

        return answer;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {

        for (String a :
                strings) {
            Log.e("Alm", "word " + a);
        }

        EventBus.getDefault().post(strings);

        super.onPostExecute(strings);
    }
}
