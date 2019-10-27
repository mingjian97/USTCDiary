package com.grouptwo.ustcdiary.topicActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grouptwo.ustcdiary.topicActivity.EditMemoDialogFragment.MemoCallback;
import com.grouptwo.ustcdiary.R;
import com.grouptwo.ustcdiary.db.DBHelper;

import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: mingjian
 * Date: 2019/10/23
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyHolder> implements ItemTouchHelperAdapter{

    private Context context;
    private List<MemoEntity> list;
    private boolean isEditMode = false;
    private DBHelper dbHelper;
    private MemoCallback callback;

    public MemoAdapter(Context context, List<MemoEntity>list,DBHelper dbHelper){
        try {
            this.callback = (MemoCallback) context;
        }catch (ClassCastException e) {
        }
        this.context=context;
        this.list=list;
        this.dbHelper=dbHelper;
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View rootView;
        private ImageView IV_memo_item_dot;
        private  TextView TV_memo_item_content;
        private ImageView IV_memo_item_delete;
        private RelativeLayout RL_memo_item_root_view;
        private int itemPosition;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.rootView=itemView;
            RL_memo_item_root_view=(RelativeLayout)rootView.findViewById(R.id.RL_memo_item_root_view);
            IV_memo_item_dot=(ImageView)rootView.findViewById(R.id.IV_memo_item_dot);
            TV_memo_item_content=itemView.findViewById(R.id.TV_memo_item_content);
            IV_memo_item_delete = (ImageView) rootView.findViewById(R.id.IV_memo_item_delete);

        }

        public void setText(String s){
            this.TV_memo_item_content.setText(s);
        }
        public TextView getText(){
            return TV_memo_item_content;
        }
        private void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        private void initView() {
            IV_memo_item_dot.setImageResource(R.drawable.ic_memo_dot_24dp);
            if (isEditMode) {
//                IV_memo_item_dot.setImageResource(R.drawable.ic_memo_swap_vert_black_24dp);
                IV_memo_item_delete.setVisibility(View.VISIBLE);
//                IV_memo_item_dot.setOnTouchListener(this);
                IV_memo_item_delete.setOnClickListener(this);
                RL_memo_item_root_view.setOnClickListener(this);
            } else {

                IV_memo_item_delete.setVisibility(View.GONE);
                IV_memo_item_dot.setOnTouchListener(null);
                IV_memo_item_delete.setOnClickListener(null);
                RL_memo_item_root_view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.IV_memo_item_delete:
                    SQLiteDatabase db= dbHelper.getWritableDatabase();
                    db.delete("memo","_id=?",new String[]{String.valueOf(list.get(itemPosition).getMemoId())});
                    callback.addMemo(2);
                    notifyDataSetChanged();
                    break;
//                case R.id.RL_memo_item_root_view:
//                    if (isEditMode) {
//                        EditMemoDialogFragment editMemoDialogFragment = EditMemoDialogFragment.newInstance(
//                                topicId, memoList.get(itemPosition).getMemoId(), false, memoList.get(itemPosition).getContent());
//                        editMemoDialogFragment.show(mActivity.getSupportFragmentManager(), "editMemoDialogFragment");
//                    } else {
//                        memoList.get(itemPosition).toggleChecked();
//                        dbManager.opeDB();
//                        dbManager.updateMemoChecked(memoList.get(itemPosition).getMemoId(), memoList.get(itemPosition).isChecked());
//                        dbManager.closeDB();
//                        setMemoContent(this, itemPosition);
//                    }
//                    break;
            }
        }
    }

    @NonNull
    @Override
    public MemoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_memo_item,parent,false);
        MyHolder myholder=new MyHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MemoEntity memo=list.get(position);
        holder.setText(memo.getContent());
        holder.setItemPosition(position);
        holder.initView();
//        if (holder instanceof MyHolder) {
//        ((MyHolder) holder).setItemPosition(position);
//            ((MyHolder) holder).initView();
//            setMemoContent(((MemoViewHolder) holder), position);
        }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwap(int position) {

    }

    @Override
    public void onItemMoveFinish() {

    }
}
