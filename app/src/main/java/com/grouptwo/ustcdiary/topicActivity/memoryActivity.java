package com.grouptwo.ustcdiary.topicActivity;
/**
 * Created with IntelliJ IDEA.
 * User: mingjian
 * Date: 2019/10/23
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grouptwo.ustcdiary.R;
import com.grouptwo.ustcdiary.db.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//备忘录activity
public class memoryActivity extends AppCompatActivity implements View.OnClickListener,EditMemoDialogFragment.MemoCallback{

    //UI
    private RelativeLayout RL_memo_topbar_content;
    private TextView TV_memotopbar_title;
    private ImageView IV_memo_edit;
    private View rootView;
    private TextView TV_memo_item_add;
    //DB
    private DBHelper dbhelper;
    //RecyclerView
    private RelativeLayout RL_memo_content_bg;
    private RecyclerView RecyclerView_memo;
    private MemoAdapter memoAdapter;
    private List<MemoEntity> memoList;
    private ItemTouchHelper touchHelper;

    //是否显示编辑界面
    boolean Flags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        dbhelper=new DBHelper(getApplicationContext(),"memo",null,1);

        //init UI
        RL_memo_topbar_content=(RelativeLayout)findViewById(R.id.RL_memo_toolbar_content);
        RL_memo_content_bg=(RelativeLayout)findViewById(R.id.RL_memo_content_bg);
        TV_memotopbar_title=(TextView)findViewById(R.id.TV_memo_topbar_title);
        IV_memo_edit=(ImageView)findViewById(R.id.IV_memo_edit);
        IV_memo_edit.setOnClickListener(this);
        rootView=findViewById(R.id.Layout_memo_item_add);
        rootView.setOnClickListener(this);
        TV_memo_item_add=(TextView)rootView.findViewById(R.id.TV_memo_item_add);
        RecyclerView_memo=(RecyclerView)findViewById(R.id.RecyclerView_memo);
        //从数据库中提取数据
        memoList=new ArrayList<>();
        loadMemo();
        initMemoAdapter();
        Flags=true;
    }
    private void initMemoAdapter() {
        //Init topic adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView_memo.setLayoutManager(layoutManager);
        RecyclerView_memo.setHasFixedSize(true);
        memoAdapter = new MemoAdapter(this, memoList,dbhelper);
        RecyclerView_memo.setAdapter(memoAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if(Flags){
                    int swipFlag=0;
                    int dragFlag=0;
                    return makeMovementFlags(dragFlag,swipFlag);
                }
                else{
                    int swipFlag=0;
                    int dragFlag=ItemTouchHelper.UP| ItemTouchHelper.DOWN;
                    return makeMovementFlags(dragFlag,swipFlag);
                }
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                memoAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                Collections.swap(memoList,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                int order=1;
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                for (MemoEntity memo : memoList) {
                    ContentValues values=new ContentValues();
                    values.put("ordernum",order++);
                    db.update("memo",values,"_id=?",new String[]{String.valueOf(memo.getMemoId())});
                }
                db.close();
                memoAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(RecyclerView_memo);
    }
    private void loadMemo() {
        memoList.clear();
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        Cursor cursor = db.query("memo", null, null,null, null, null, "ordernum asc");
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            MemoEntity memo=new MemoEntity(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3));
            System.out.println(memo.getMemoId()+"-----"+memo.getContent()+"------"+memo.getIsDeleted()+"--------"+memo.getOrderNum());
            if(memo.getIsDeleted()==0){
                memoList.add(memo);
            }
        }
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.IV_memo_edit:
                memoAdapter.setEditMode(Flags);
                memoAdapter.notifyDataSetChanged();
                if(Flags){
                    rootView.setVisibility(View.VISIBLE);
                    IV_memo_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_cancel_white_24dp));
                    Flags=!Flags;
                    }
                else{
                    rootView.setVisibility(View.GONE);
                    IV_memo_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
                    Flags=!Flags;
                }
                break;
            case R.id.Layout_memo_item_add:
                EditMemoDialogFragment editMemoDialogFragment=EditMemoDialogFragment.newInstance(-1,true,"");
                editMemoDialogFragment.show(getSupportFragmentManager(),"editMemoDialogFragment");
        }
    }
    public void addMemo(int flag){
        if(flag==1){
            SQLiteDatabase db=dbhelper.getWritableDatabase();
            Cursor cursor = db.query("memo", null, "isDeleted=?",new String[]{"0"}, null, null, "_id desc");
            //利用游标遍历所有数据对象
            cursor.moveToFirst();
            memoList.add(0,new MemoEntity(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3)));
            cursor.close();
        }
        else if(flag==2){
            loadMemo();
        }
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateMemo() {
        loadMemo();
        memoAdapter.notifyDataSetChanged();
    }
}
