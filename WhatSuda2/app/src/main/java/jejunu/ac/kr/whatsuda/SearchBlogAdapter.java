package jejunu.ac.kr.whatsuda;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HSH on 16. 9. 11..
 */
public class SearchBlogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SearchResult> arrayList;

    public SearchBlogAdapter(Context context, ArrayList<SearchResult> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = View.inflate(context, R.layout.search_blog_row, null);
            viewHolder.tvTitle = (TextView) v.findViewById(R.id.tv_title);
            viewHolder.tvDescription = (TextView) v.findViewById(R.id.tv_description);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        final SearchResult result = (SearchResult) getItem(position);
        viewHolder.tvTitle.setText(Html.fromHtml(result.getTitle()));
        viewHolder.tvDescription.setText(Html.fromHtml(result.getDescription()));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getUrl()));
                context.startActivity(intent);
            }
        });
        return v;
    }

    class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
    }
}
