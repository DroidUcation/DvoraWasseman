package com.finalproject.course.finalproject.recyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.finalproject.course.finalproject.R;
import com.finalproject.course.finalproject.utils.DateFormatUtil;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;


/**
* Provide views to RecyclerView with data from productList.
*/
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subTitle, text;
        private ImageView productImg;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            productImg = (ImageView) view.findViewById(R.id.row_img);
        }
    }


    public ProductsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getProductName());
        holder.subTitle.setText(product.getStoreName());
        holder.text.setText("Added by "+
                            product.getUserID().toString()+ " on " +
                            DateFormatUtil.DATE_FORMAT_DDMMYYYY.format(product.getDateModified())); //TODO: Get user name and use strings file
        holder.productImg.setImageResource(product.getImage()); //TODO: Get product image
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
