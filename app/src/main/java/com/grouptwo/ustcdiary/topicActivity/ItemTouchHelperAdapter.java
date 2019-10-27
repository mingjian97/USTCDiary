package com.grouptwo.ustcdiary.topicActivity;

/**
 * Created with IntelliJ IDEA.
 * User: mingjian
 * Date: 2019/10/23
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwap(int position);

    void onItemMoveFinish();
}
