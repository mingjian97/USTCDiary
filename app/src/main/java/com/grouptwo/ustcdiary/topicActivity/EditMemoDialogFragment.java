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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.grouptwo.ustcdiary.R;
import com.grouptwo.ustcdiary.db.DBHelper;

/**
 * Created with IntelliJ IDEA.
 * User: mingjian
 * Date: 2019/10/23
 * 备忘录弹窗，用于对备忘录的填写
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
    /**
     * Info
     */
    //default = -1 , it means add memo.
    private long memoId = -1;
    private boolean isAdd = true;
    private String memoContent = "";

    public static EditMemoDialogFragment newInstance(long memoId, boolean isAdd, String memoContent) {
        Bundle args = new Bundle();
        EditMemoDialogFragment fragment = new EditMemoDialogFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memoId=getArguments().getLong("memoId",-1L);
        isAdd=getArguments().getBoolean("isAdd",true);
        memoContent=getArguments().getString("memoContent","");
        EDT_edit_memo_content.setText(memoContent);
        //For show keyboard
        EDT_edit_memo_content.requestFocus();
        getDialog().getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    public void okButtonEvent(){
        if (EDT_edit_memo_content.getText().toString().length() > 0) {
            if (isAdd) {
                SQLiteDatabase db=dbhelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("content",EDT_edit_memo_content.getText().toString());
                values.put("isDeleted",0);
                db.insert("memo",null,values);
                callback.addMemo(1);
                db.close();
            } else {
                if(memoId!=-1){
                    System.out.println("---------------------------------");
                    SQLiteDatabase db=dbhelper.getWritableDatabase();
                    ContentValues value=new ContentValues();
                    value.put("content",EDT_edit_memo_content.getText().toString());
                    db.update("memo",value,"_id=?",new String[]{String.valueOf(memoId)});
                    db.close();
                }
                callback.updateMemo();
            }
            dismiss();
        } else {
            Toast.makeText(getActivity(),"不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_edit_memo_cancel:
                dismiss();
                break;
            case R.id.btn_edit_memo_ok:
                okButtonEvent();
                dismiss();
                break;

        }

    }
}
