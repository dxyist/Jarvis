
package com.ecnu.leon.jarvis.Utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 封装常用对话框类
 */

public class DialogUtil {

    /**
     * （未调试）创建一仅标题和内容的Dialog，再利用builder初始化，在返回对应的Dialog对象。
     *
     * @param context         申请位置的上下文
     * @param iconId          对应图标的ID位置，例如R.ID.*。如果为-1代表无图标。
     * @param title           对应对话框标题栏
     * @param message         对应对话框显示栏
     * @param btnName         对应按钮名称
     * @param onClickListener 对应按钮的监听器 (必填)
     * @return 返回对应的Dialog对象
     */
    public static Dialog createNormalDialog(Context context, int iconId, String title, String message, String btnName, DialogInterface.OnClickListener onClickListener) {

        Dialog dialog = null;
        Builder builder = new Builder(context);
        if (iconId > 0)
            builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnName, onClickListener);
        dialog = builder.show();
        return dialog;
    }

    /**
     * （已调试）生成一个含有List列表的Dialog
     *
     * @param context         申请位置的上下文
     * @param iconId          对应图标的ID位置，例如R.ID.*。如果为-1代表无图标。
     * @param title           对应对话框标题栏
     * @param itemsStrings    对应String数组，确定items名称
     * @param onClickListener 对应listener，用Switch 来对应数组内容
     * @return 返回对应对话框
     */

    public static Dialog createListDialog(Context context, int iconId, String title, String[] itemsStrings, DialogInterface.OnClickListener onClickListener) {

        Dialog dialog = null;
        Builder builder = new Builder(context);
        if (iconId > 0)
            builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setItems(itemsStrings, onClickListener);
        dialog = builder.show();
        return dialog;
    }

}

