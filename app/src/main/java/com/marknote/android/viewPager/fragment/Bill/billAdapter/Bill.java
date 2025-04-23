package com.marknote.android.viewPager.fragment.Bill.billAdapter;

public class Bill {
    private String time;
    private String money;
    private String type;



    private String category;

    private String comment;
    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }
    public String getTime() {
        return time;
    }

    public String getMoney() {
        return money;
    }

    public String getComment() {
        return comment;
    }

    public Bill(String time,String money,String type,String comment){
        this.time=time;
       this.money=money;
       this.comment=comment;
       this.type = type;

   }
    public Bill(String time,String money,String type,String comment,String category){
        this.time=time;
        this.money=money;
        this.comment=comment;
        this.type = type;
        this.category = category;

    }
}
