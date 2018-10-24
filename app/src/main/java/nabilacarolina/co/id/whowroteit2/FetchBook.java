package nabilacarolina.co.id.whowroteit2;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

class FetchBook extends AsyncTask<String,Void,String>{

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    FetchBook(TextView mTitleText, TextView mAuthorText, TextView titleText, TextView authorText) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
    }

    public FetchBook(TextView mTitleText, TextView mAuthorText) {

    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return NetworkUtils.getBookInfo(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {

            JSONObject jsonObject = new JSONObject(s);

            JSONArray itemsArray = jsonObject.getJSONArray("items");


            int i = 0;
            String title = null;
            String authors = null;


            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {

                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");


                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                i++;
            }


            if (title != null && authors != null) {
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            } else {

                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

        } catch (Exception e) {

            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
        }
    }

}
