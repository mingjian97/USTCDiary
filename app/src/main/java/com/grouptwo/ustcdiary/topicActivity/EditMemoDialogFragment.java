package com.grouptwo.ustcdiary.topicActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.grouptwo.ustcdiary.R;
import com.grouptwo.ustcdiary.db.DBHelper;

/**
 * Created with IntelliJ IDEA.
 * User: mingjian
 * Date: 2019/10/23
 */
public class EditMemoDialogFragment extends DialogFragment implements View.OnClickListener {

//callback
    public interface MemoCallback{
        void addMemo(int memo);
        void updateMemo();
    }

    private MemoCallback callback;

//    UI
    private Button btn_edit_memo_ok;
    private Button btn_edit_memo_cancel;
    private EditText EDT_edit_memo_content;
    private DBHelper dbhelper;

    public static EditMemoDialogFragment newInstance(long topicId, long memoId, boolean isAdd, String memoContent) {
        Bundle args = new Bundle();
        EditMemoDialogFragment fragment = new EditMemoDialogFragment();
        args.putLong("topicId", topicId);
        args.putLong("memoId", memoId);
        args.putBoolean("isAdd", isAdd);
        args.putString("memoContent", memoContent);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper=new DBHelper(getActivity(),"memo",null,1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.getDialog().setCanceledOnTouchOutside(false);
        View rootView=inflater.inflate(R.layout.dialog_fragment_edit_memo,container);

        EDT_edit_memo_content=(EditText)rootView.findViewById(R.id.EDT_edit_memo_content);
        btn_edit_memo_cancel=(Button)rootView.findViewById(R.id.btn_edit_memo_cancel);
        btn_edit_memo_ok=(Button)rootView.findViewById(R.id.btn_edit_memo_ok);
        btn_edit_memo_cancel.setOnClickListener(this);
        btn_edit_memo_ok.setOnClickListener(this);



        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        try {
            callback = (MemoCallback) context;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_edit_memo_cancel:
                dismiss();
                break;
            case R.id.btn_edit_memo_ok:
                if(EDT_edit_memo_content.getText().toString().length()>0){
                    SQLiteDatabase db=dbhelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("content",EDT_edit_memo_content.getText().toString());
                    values.put("isDeleted",0);
                    db.insert("memo",null,values);
                    callback.addMemo(1);
//                    Cursor cursor = db.query("memo", null, null, null, null, null, null);
                    //利用游标遍历所有数据对象
                    //为了显示全部，把所有对象连接起来，放到TextView中
//                    String textview_data = "111";
//                    while(cursor.moveToNext()){
//                        String content = cursor.getString(1);
//                        textview_data = textview_data + "," + content;
//                    }
//                    System.out.println(textview_data);

                    db.close();

                }
                dismiss();
                break;

        }

    }
}
