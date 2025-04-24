package com.marknote.android.viewPager.fragment.Bill.billAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<Bill> mBillList;
    //BillAdapter的构造方法
    public BillAdapter(List<Bill> billList) {
        mBillList = billList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView money;
        TextView comment;

        public ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.item_bill_time);
            money = view.findViewById(R.id.item_bill_money);
            comment = view.findViewById(R.id.item_bill_comment);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bill_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
Bill bill=mBillList.get(position);
holder.time.setText(bill.getTime());

holder.comment.setText(bill.getComment());
String type=bill.getType();
if (type.equals("支出")) {
            holder.money.setTextColor(0xffff0000); // 红色
            holder.money.setText("-" + bill.getMoney());
        } else {
            holder.money.setTextColor(0xff00ff00); // 绿色
            holder.money.setText("+" + bill.getMoney());
        }
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }

    public void updateBills(List<Bill> bills) {
        mBillList = bills;
        notifyDataSetChanged();
    }

}
