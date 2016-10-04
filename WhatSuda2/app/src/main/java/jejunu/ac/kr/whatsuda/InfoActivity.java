package jejunu.ac.kr.whatsuda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    private ListView listView;
    private SearchBlogAdapter adapter;
    private static final String CLIENT_ID = "6Ympg8b1duWn3d4ExxuL";
    private static final String CLIENT_SECRET = "mmgt84pcvt";
    private ArrayList<SearchResult> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listView = (ListView) findViewById(R.id.listView_search_blog);
        arrayList = new ArrayList<>();

        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        new SearchBlogTask(this).execute(name);
    }

    class SearchBlogTask extends AsyncTask<String, String, String> {

        Context context;
        ProgressDialog progressDialog;
        private static final String TAG = "SearchBlogTask";

        SearchBlogTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("관련 내용을 검색합니다.");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String text = URLEncoder.encode(params[0], "UTF-8");
                String urlStr = "https://openapi.naver.com/v1/search/blog.xml?query=" + text + "&display=10&start=1";
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
                connection.setRequestProperty("Content-Type", "application/xml");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                // 스트림을 통해 파싱을 한다.
                parser.setInput(connection.getInputStream(), null);
                parser.next();

                boolean isItem = false;
                boolean isTitle = false;
                boolean isDescription = false;
                boolean isUrl = false;
                int parserEvent = parser.getEventType();
                SearchResult searchResult = null;
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_DOCUMENT:
                            Log.d(TAG, "START_DOCUMENT");
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                isItem = true;
                                searchResult = new SearchResult();
                            } else if (parser.getName().equals("title")) {
                                isTitle = true;
                            } else if (parser.getName().equals("description")) {
                                isDescription = true;
                            } else if (parser.getName().equals("link")) {
                                isUrl = true;
                            }
                            Log.d(TAG, "START_TAG");
                            Log.d(TAG, parser.getName());
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                arrayList.add(searchResult);
                                isItem = false;
                            } else if (parser.getName().equals("title")) {
                                isTitle = false;
                            } else if (parser.getName().equals("description")) {
                                isDescription = false;
                            } else if (parser.getName().equals("link")) {
                                isUrl = false;
                            }
                            Log.d(TAG, "END_TAG");
                            Log.d(TAG, parser.getName());
                            break;
                        case XmlPullParser.TEXT:
                            if (isItem) {
                                if (isTitle) {
                                    Log.d(TAG, "is title true");
                                    searchResult.setTitle(parser.getText());
                                } else if (isDescription) {
                                    Log.d(TAG, "is description true");
                                    searchResult.setDescription(parser.getText());
                                } else if (isUrl) {
                                    Log.d(TAG, "is url true");
                                    searchResult.setUrl(parser.getText());
                                }
                            }
                            Log.d(TAG, "TEXT");
                            Log.d(TAG, parser.getText());
                            break;
                    }
                    parserEvent = parser.next();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            for (SearchResult searchResult1 : arrayList) {
                Log.d(TAG, searchResult1.getTitle());
                Log.d(TAG, searchResult1.getDescription());
                Log.d(TAG, searchResult1.getUrl());
            }

            adapter = new SearchBlogAdapter(InfoActivity.this, arrayList);
            listView.setAdapter(adapter);
        }
    }
}
