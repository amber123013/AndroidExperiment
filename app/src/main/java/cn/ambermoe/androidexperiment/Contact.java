package cn.ambermoe.androidexperiment;

/**
 * Created by ASUS on 2017-03-17.
 */

public class Contact {
    /**联系人 姓名*/
    private String mName;
    /**联系人 职位信息*/
    private String mMessage;
    /**联系人 电话号码*/
    private String mPhoneNumber;
    /**联系人 显示姓氏颜色*/
    private int mSurnameColor;

    public Contact(String name,String message,String phoneNumber,int surnameColor) {
        this.mName = name;
        this.mMessage = message;
        this.mPhoneNumber = phoneNumber;
        this.mSurnameColor = surnameColor;
    }

    /**
     * 返回联系人信息
     * @return
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * 返回联系人姓名
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     * 返回联系人电话号码
     * @return
     */
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    /**
     * 返回联系人姓氏背景颜色
     * @return
     */
    public int getSurnameColor() {
        return mSurnameColor;
    }
}
