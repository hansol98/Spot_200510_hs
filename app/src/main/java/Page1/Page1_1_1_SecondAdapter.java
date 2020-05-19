package Page1;

import android.content.Context;

import DB.DbOpenHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.hansol.spot_200510_hs.OnItemClick;
import com.example.hansol.spot_200510_hs.R;

import java.util.ArrayList;
import java.util.List;

public class Page1_1_1_SecondAdapter extends RecyclerView.Adapter<Page1_1_1_SecondAdapter.ViewHolder> {
    Context context;
    private String[] stay = new String[5];  // 하트의 클릭 여부
    private List<Page1_1_1.Recycler_item> items;  //리사이클러뷰 안에 들어갈 값 저장
    private OnItemClick mCallback;

    long nowIndex;
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    ArrayAdapter<String> arrayAdapter;

    String sort = "userid";

    public Page1_1_1_SecondAdapter(List<Page1_1_1.Recycler_item> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page1_1_1_second_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Page1_1_1.Recycler_item item=items.get(position);

        holder.title.setText(items.get(position).title);
//        //이미지뷰에 url 이미지 넣기.
//        Glide.with(context).load(item.getImage()).centerCrop().into(holder.imageView);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(context).load(item.getImage()).apply(requestOptions).into(holder.imageView);

        holder.type.setText(item.type);

//        holder.heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (stay[position] == null) {
////                    holder.heart.setBackgroundResource(R.drawable.ic_icon_addmy);
////                    mCallback.make_db(item.getContentviewID(), item.getTitle());   //countId랑 title을 db에 넣으려고 함( make_db라는 인터페이스 이용)
////                    mCallback.make_dialog();                                       //db에 잘 넣으면 띄우는 다이얼로그(위와 마찬가지로 인터페이스 이용
////                    stay[position] = "ON";
//                     Toast.makeText(context,"관심관광지를 눌렀습니다",Toast.LENGTH_SHORT).show();
//
//                } else {
//                    holder.heart.setBackgroundResource(R.drawable.ic_heart_off);
////                    stay[position] = null;
////                    nowIndex = Long.parseLong(arrayIndex.get(position));
////                    String[] nowData = arrayData.get(position).split("\\s+");
////                    String viewData = nowData[0];
////                    mDbOpenHelper.deleteColumn(nowIndex);
////                    showDatabase(sort);
//                     Toast.makeText(context,"관심관광지를 취소했습니다",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView type;
        TextView title;
        Button heart;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.no_image);
            type = itemView.findViewById(R.id.page1_1_1_cardview_type);
            title = itemView.findViewById(R.id.page1_1_1_cardview_title);
            heart = itemView.findViewById(R.id.page1_1_1_cardview_heart);
        }
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.selectColumns();
        //iCursor.moveToFirst();
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();

        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            tempID = setTextLength(tempID,10);
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);

            String Result = tempID + tempName;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }

        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }
}
