package hr.gregl.goldenhourphotography.adapter


import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.TIME_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSunrise = itemView.findViewById<TextView>(R.id.tvSunrise)
        private val tvSunset = itemView.findViewById<TextView>(R.id.tvSunset)
        // TODO Additional TextViews for other data...

        fun bind(item: Item) {
            tvSunrise.text = "Sunrise: ${item.sunrise}"
            tvSunset.text = "Sunset: ${item.sunset}"
            // TODO Bind other TextViews...
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener {
            // TODO Define what happens on long click
            true
        }

        holder.itemView.setOnClickListener {
            // TODO Define what happens on click
        }

        holder.bind(items[position])
    }
}