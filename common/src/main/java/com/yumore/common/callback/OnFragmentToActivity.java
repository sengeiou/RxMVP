package com.yumore.common.callback;

/**
 * 建议使用key->value的形式出传数据
 * 但不要传大量数据
 * T->Parameter(String key, Object value)
 *
 * @author Nathaniel
 * @date 2019/4/19 - 18:42
 */
public interface OnFragmentToActivity<T> {

    String ACTION_DELETE_PIGEON = "deletePigeon";
    String ACTION_UPDATE_PIGEON = "updatePigeon";
    String ACTION_UPDATE_UNREAD = "updateUnread";

    /**
     * 获取指定fragment的数据
     *
     * @param position position
     * @param action   action
     * @param t        data
     */
    void onCallback(int position, String action, T t);
}
