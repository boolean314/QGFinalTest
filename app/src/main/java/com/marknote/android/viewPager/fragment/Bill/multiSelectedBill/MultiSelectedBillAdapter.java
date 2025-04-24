package com.marknote.android.viewPager.fragment.Bill.multiSelectedBill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.viewPager.fragment.Bill.billAdapter.Bill;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectedBillAdapter extends RecyclerView.Adapter<MultiSelectedBillAdapter.ViewHolder> {
    private List<Bill> mBillList;

    //记录每个项目的选中状态
    private List<Boolean> mCheckedStatus = new ArrayList<>();
    //选中状态变化监听器
    private OnItemCheckedChangeListener mCheckedListener;

    // BillAdapter的构造方法
    public MultiSelectedBillAdapter(List<Bill> billList) {
        mBillList = billList;
        // 初始化所有项目为未选中状态
        for (int i = 0; i < billList.size(); i++) {
            mCheckedStatus.add(false);
        }
    }

    //选中状态变化监听接口
    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(int position, boolean isChecked);
    }

    //获取选中状态的Bill列表
    public List<Bill> getSelectedBills() {
        List<Bill> selectedBills = new ArrayList<>();
        for (int i = 0; i < mBillList.size(); i++) {
            if (mCheckedStatus.get(i)) {
                selectedBills.add(mBillList.get(i));
            }
        }
        return selectedBills;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView money;
        TextView comment;
        CheckBox checkBox;
        public ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.multi_item_bill_time);
            money = view.findViewById(R.id.multi_item_bill_money);
            comment = view.findViewById(R.id.multi_item_bill_comment);
            checkBox = view.findViewById(R.id.bill_check_selected);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_multi_selected_bill_item,parent,false);
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
        holder.checkBox.setChecked(mCheckedStatus.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckedStatus.set(position, isChecked);
                if (mCheckedListener != null) {
                    mCheckedListener.onItemCheckedChange(position, isChecked);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return mBillList.size();
    }

}
