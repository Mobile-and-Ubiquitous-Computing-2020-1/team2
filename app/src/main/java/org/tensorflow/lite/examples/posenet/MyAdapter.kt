package org.tensorflow.lite.examples.posenet

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter(private val myDataset: MutableList<MemberDTO>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view : View ) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        var d : MemberDTO = myDataset.get(position)
        if(d.ranking_num == "1") {
            holder.view.findViewById<CircleImageView>(R.id.profile_image)
                .setImageResource(R.drawable.crown);
            holder.view.findViewById<RelativeLayout>(R.id.item_layout).setBackgroundColor(holder.view.context.getColor(R.color.tfe_color_primary))

        }

        if(d.ranking_num == "Ranking"){
            val c_layout = holder.view.findViewById<TextView>(R.id.ranking_score).parent as LinearLayout
            c_layout.setBackgroundColor(holder.view.context.getColor(android.R.color.white))
        }
        //holder.view.findViewById<TextView>(R.id.ranking_number).setText(ranking_num);
        holder.view.findViewById<TextView>(R.id.ranking_number).setText(d.ranking_num);
        holder.view.findViewById<TextView>(R.id.user_name).setText(d.name);
        holder.view.findViewById<TextView>(R.id.ranking_score).setText(d.score);
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}