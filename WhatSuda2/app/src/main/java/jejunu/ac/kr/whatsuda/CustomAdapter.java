package jejunu.ac.kr.whatsuda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by HSH on 16. 9. 6..
 */
public class CustomAdapter extends BaseAdapter {

    private final String TAG = "CustomAdapter";
    private ArrayList<LandMark> landMarkList;
    private Context context;
    private int count;
    private DecimalFormat decimalFormat;

    public CustomAdapter(Context context, Map<Integer, LandMark> landMarkMap, int start, int end) {
        this.context = context;
        landMarkList = new ArrayList();
        for (int i = start; i <= end; i++) {
            landMarkList.add(landMarkMap.get(i));
            Log.d(TAG, "i : " + i);
        }
        Log.d(TAG, "size : " + landMarkList.size());
        count = 0;
        decimalFormat = new DecimalFormat("#,##0");
    }

    @Override
    public int getCount() {
        return landMarkList.size();
    }

    @Override
    public LandMark getItem(int position) {
        return landMarkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = View.inflate(context, R.layout.list_item_row, null);
            viewHolder.tvNumber = (TextView) v.findViewById(R.id.tv_num);
            viewHolder.tvName = (TextView) v.findViewById(R.id.tv_name);
            viewHolder.tvAverage = (TextView) v.findViewById(R.id.tv_average);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InfoActivity.class);
                    intent.putExtra("title", getItem(position).getName());
                    context.startActivity(intent);
                }
            });
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        LandMark item = getItem(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvNumber.setText(position + 1 + "");
        viewHolder.tvAverage.setText("연 평균 : " + decimalFormat.format(item.getCount()));
        return v;
    }

    class ViewHolder {
        public TextView tvNumber;
        public TextView tvName;
        public TextView tvAverage;
    }
}
